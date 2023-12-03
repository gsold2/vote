package com.github.gsold2.vote.web.dish;

import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.repository.DishRepository;
import com.github.gsold2.vote.service.DishService;
import com.github.gsold2.vote.to.DishTo;
import com.github.gsold2.vote.util.DishUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final DishService service;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @GetMapping
    @Cacheable(key = "#restaurantId")
    public List<DishTo> getAllByRestaurant(@RequestParam int restaurantId) {
        log.info("get all dishes for restaurant {}", restaurantId);
        return DishUtil.getTos(dishRepository.getAllByRestaurant(restaurantId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {}", id);
        assureIdConsistent(dish, id);
        service.update(id, dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish,
                                                   @RequestParam int restaurantId) {
        log.info("create {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        Dish created = service.create(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
