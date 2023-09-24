package ru.bootjava.vote.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.vote.model.Dish;
import ru.bootjava.vote.repository.DishRepository;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.service.DishService;
import ru.bootjava.vote.to.DishTo;
import ru.bootjava.vote.util.DishUtil;
import ru.bootjava.vote.web.AuthUser;

import java.net.URI;
import java.util.List;

import static ru.bootjava.vote.util.validation.ValidationUtil.assureIdConsistent;
import static ru.bootjava.vote.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {

    static final String REST_URL = "/api/dishes";

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;
    private final DishService service;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(dishRepository.get(authUser.id(), id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Dish dish = dishRepository.getExistedAndBelonged(authUser.id(), id);
        dishRepository.delete(dish);
    }

    @GetMapping
    public List<DishTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll for user {}", authUser.id());
        return DishUtil.getTos(dishRepository.getAll(authUser.id()));
    }

    @GetMapping("/filter")
    public List<DishTo> getAllByRestaurant(@AuthenticationPrincipal AuthUser authUser, @RequestParam @Nullable int restaurantId) {
        log.info("getAll for the user {} and restaurant{}", authUser.id(), restaurantId);
        return DishUtil.getTos(dishRepository.getAllByRestaurant(authUser.id(), restaurantId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Dish dish, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", dish, userId);
        assureIdConsistent(dish, id);
        int restaurantId = dishRepository.getExistedAndBelonged(userId, id).getRestaurant().id();
        service.save(restaurantId, dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Dish dish, @RequestParam @NonNull int restaurantId) {
        int userId = authUser.id();
        log.info("create {} for restaurant {} by user {}", dish, restaurantId, userId);
        checkNew(dish);
        restaurantRepository.getExistedAndBelonged(userId, restaurantId);
        Dish created = service.save(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
