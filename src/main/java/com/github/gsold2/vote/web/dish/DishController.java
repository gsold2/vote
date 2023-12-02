package com.github.gsold2.vote.web.dish;

import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.repository.DishRepository;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.service.DishService;
import com.github.gsold2.vote.to.DishTo;
import com.github.gsold2.vote.util.DishUtil;
import com.github.gsold2.vote.web.AuthUser;
import jakarta.validation.Valid;
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

import java.net.URI;
import java.util.List;

import static com.github.gsold2.vote.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.gsold2.vote.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "dishes")
public class DishController {
    static final String REST_URL = "/api/admin/dishes";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishService service;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get dish {} for user {}", id, authUser.id());
        return ResponseEntity.of(dishRepository.get(id));
    }

    @GetMapping
    @Cacheable(key = "#restaurantId")
    public List<DishTo> getAllByRestaurant(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        log.info("get all dishes for restaurant {} and user {}", restaurantId, authUser.id());
        return DishUtil.getTos(dishRepository.getAllByRestaurant(restaurantId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Dish dish, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", dish, userId);
        assureIdConsistent(dish, id);
        int restaurantId = dishRepository.getIfExisted(id).getRestaurant().id();
        service.save(restaurantId, dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @Valid @RequestBody Dish dish,
                                                   @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for restaurant {} and user {}", dish, restaurantId, userId);
        checkNew(dish);
        restaurantRepository.getExisted(restaurantId);
        Dish created = service.save(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
