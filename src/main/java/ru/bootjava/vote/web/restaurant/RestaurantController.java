package ru.bootjava.vote.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.service.RestaurantService;
import ru.bootjava.vote.to.RestaurantTo;
import ru.bootjava.vote.util.RestaurantsUtil;
import ru.bootjava.vote.web.AuthUser;

import java.net.URI;
import java.util.List;

import static ru.bootjava.vote.util.validation.ValidationUtil.assureIdConsistent;
import static ru.bootjava.vote.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantService service;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(repository.get(authUser.id(), id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Restaurant restaurant = repository.getExistedAndBelonged(authUser.id(), id);
        repository.delete(restaurant);
    }

    @GetMapping
    public List<RestaurantTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all restaurants for user {}", authUser.id());
        return RestaurantsUtil.getTos(repository.getAll(authUser.id()));
    }

    @GetMapping("/filter")
    public List<Restaurant> getAllWithMenuUpToday(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all restaurants with menu for user {} up today", authUser.id());
        return repository.getAllWithMenuUpToday(authUser.id());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", restaurant, userId);
        assureIdConsistent(restaurant, id);
        repository.getExistedAndBelonged(userId, id);
        service.save(userId, restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant) {
        int userId = authUser.id();
        log.info("create {} for user {}", restaurant, userId);
        checkNew(restaurant);
        Restaurant created = service.save(userId, restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}