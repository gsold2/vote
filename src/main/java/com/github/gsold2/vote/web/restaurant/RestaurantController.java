package com.github.gsold2.vote.web.restaurant;

import com.github.gsold2.vote.model.Restaurant;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.service.RestaurantService;
import com.github.gsold2.vote.to.RestaurantTo;
import com.github.gsold2.vote.util.RestaurantsUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantService service;

    @GetMapping(REST_URL + "/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping(REST_URL)
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return repository.getAll();
    }

    @GetMapping(value = {"/api/user/restaurants/with-menu-up-today", "/api/admin/restaurants/with-menu-up-today"})
    @Cacheable(value = "restaurants")
    public List<RestaurantTo> getAllWithMenuUpToday() {
        log.info("get all restaurants with menu up today");
        return RestaurantsUtil.getTos(repository.getAllWithMenuUpToday());
    }

    @PutMapping(REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam String name, @PathVariable int id) {
        log.info("update {}", id);
        service.update(id, name);
    }

    @PostMapping(REST_URL)
    public ResponseEntity<Restaurant> createWithLocation(@RequestParam String name) {
        Restaurant restaurant = new Restaurant(null, name);
        log.info("create {}", restaurant);
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}