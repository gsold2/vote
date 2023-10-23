package ru.bootjava.vote.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import ru.bootjava.vote.util.validation.DateTimeValidator;
import ru.bootjava.vote.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.bootjava.vote.util.DateUtil.atEndDayOrMax;
import static ru.bootjava.vote.util.DateUtil.atStartDayOrMin;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "votes")
public class VoteController {
    static final String REST_URL = "/api/user/votes";

    private final DateTimeValidator dateTimeValidator;
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
    @CacheEvict(allEntries = true)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Vote vote = voteRepository.getExistedAndBelonged(authUser.id(), id);
        dateTimeValidator.checkDateAndTime(vote);
        voteRepository.delete(vote);
    }

    @GetMapping
    @Cacheable(key = "#authUser.authUser().id")
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
    @CacheEvict(allEntries = true)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam @NonNull int restaurantId, @PathVariable int id) {
        int userId = authUser.id();
        Vote vote = voteRepository.getExistedAndBelonged(userId, id);
        log.info("update {} for user {}", vote, userId);
        dateTimeValidator.checkDateAndTime(vote);
        restaurantRepository.getExisted(restaurantId);
        service.save(userId, restaurantId, vote);
    }

    @PostMapping
    @CacheEvict(allEntries = true)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @RequestParam @NonNull int restaurantId) {
        int userId = authUser.id();
        log.info("create for restaurant {} by user {}", restaurantId, userId);
        restaurantRepository.getExisted(restaurantId);
        Vote vote = new Vote(null, LocalDate.now());
        Vote created = service.save(userId, restaurantId, vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
