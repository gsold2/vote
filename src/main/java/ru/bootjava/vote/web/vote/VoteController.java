package ru.bootjava.vote.web.vote;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.repository.VoteRepository;
import ru.bootjava.vote.service.VoteService;
import ru.bootjava.vote.to.VoteTo;
import ru.bootjava.vote.util.VoteUtil;
import ru.bootjava.vote.util.validation.DateTimeValidation;
import ru.bootjava.vote.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.bootjava.vote.util.DateUtil.atEndDayOrMax;
import static ru.bootjava.vote.util.DateUtil.atStartDayOrMin;
import static ru.bootjava.vote.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final DateTimeValidation dateTimeValidation;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteService service;

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(voteRepository.get(authUser.id(), id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Vote vote = voteRepository.getExistedAndBelonged(authUser.id(), id);
        dateTimeValidation.checkDateAndTime(vote);
        voteRepository.delete(vote);
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll for user {}", authUser.id());
        return VoteUtil.getTos(voteRepository.getAll(authUser.id()));
    }

    @GetMapping("/filter")
    public List<VoteTo> getBetweenWithInclusion(@AuthenticationPrincipal AuthUser authUser,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = authUser.id();
        log.info("get between dates({} - {}) for user {}", startDate, endDate, userId);
        List<Vote> votesDateFiltered = voteRepository.getBetweenWithInclusion(userId, atStartDayOrMin(startDate), atEndDayOrMax(endDate));
        return VoteUtil.getTos(votesDateFiltered);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", vote, userId);
        assureIdConsistent(vote, id);
        voteRepository.getExistedAndBelonged(userId, id);
        dateTimeValidation.checkDateAndTime(vote);
        int restaurantId = vote.getRestaurant().id();
        restaurantRepository.getExisted(restaurantId);
        service.save(userId, restaurantId, vote);
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @RequestParam @NonNull int restaurantId) {
        int userId = authUser.id();
        log.info("create for restaurant {} by user {}", restaurantId, userId);
        dateTimeValidation.checkTime();
        restaurantRepository.getExisted(restaurantId);
        Vote vote = new Vote(null, LocalDate.now());
        Vote created = service.save(userId, restaurantId, vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
