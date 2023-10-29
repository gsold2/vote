package ru.bootjava.vote.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class RestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantService service;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(repository.get(authUser.id(), id));
    }

    @GetMapping
    @Cacheable(key = "#authUser.authUser().id")
    public List<RestaurantTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all restaurants for user {}", authUser.id());
        return RestaurantsUtil.getTos(repository.getAll(authUser.id()));
    }

    @GetMapping("/with-menu-up-today")
    public List<Restaurant> getAllWithMenuUpToday(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all restaurants with menu for user {} up today", authUser.id());
        return repository.getAllWithMenuUpToday(authUser.id());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam String name, @PathVariable int id) {
        int userId = authUser.id();
        Restaurant restaurant = repository.getExistedAndBelonged(userId, id);
        log.info("update {} for user {}", restaurant, userId);
        restaurant.setName(name);
        service.save(userId, restaurant);
    }

    @PostMapping()
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam String name) {
        int userId = authUser.id();
        Restaurant restaurant = new Restaurant(null, name);
        log.info("create {} for user {}", restaurant, userId);
        Restaurant created = service.save(userId, restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}