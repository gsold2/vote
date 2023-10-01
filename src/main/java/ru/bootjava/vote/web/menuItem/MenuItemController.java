package ru.bootjava.vote.web.menuItem;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.repository.DishRepository;
import ru.bootjava.vote.repository.MenuItemRepository;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.service.MenuItemService;
import ru.bootjava.vote.to.IdTo;
import ru.bootjava.vote.to.MenuItemTo;
import ru.bootjava.vote.util.MenuItemUtil;
import ru.bootjava.vote.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.bootjava.vote.util.validation.ValidationUtil.assureIdConsistent;
import static ru.bootjava.vote.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuItemController {
    static final String REST_URL = "/api/menu-items";

    private final MenuItemRepository menuItemRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemService service;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get restaurant {} for user {}", id, authUser.id());
        return ResponseEntity.of(menuItemRepository.get(authUser.id(), id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        MenuItem menuItem = menuItemRepository.getExistedAndBelonged(authUser.id(), id);
        menuItemRepository.delete(menuItem);
    }

    @GetMapping("/filter")
    public List<MenuItemTo> getAllByRestaurantAndDate(@AuthenticationPrincipal AuthUser authUser,
                                                      @RequestParam @NonNull int restaurantId,
                                                      @RequestParam @NonNull LocalDate date) {
        log.info("getAll for the user {} and restaurant{} with date{}", authUser.id(), restaurantId, date);
        return MenuItemUtil.getTos(menuItemRepository.getAllByRestaurantAndDate(authUser.id(), restaurantId, date));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", menuItem, userId);
        assureIdConsistent(menuItem, id);
        int dishId = menuItemRepository.getExistedAndBelonged(userId, id).getDish().id();
        service.save(dishId, menuItem);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                       @Valid @RequestBody MenuItem menuItem,
                                                       @RequestParam @NonNull int dishId) {
        int userId = authUser.id();
        log.info("create {} for dish {} by user {}", menuItem, dishId, userId);
        checkNew(menuItem);
        dishRepository.getExistedAndBelonged(userId, dishId);
        MenuItem created = service.save(dishId, menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/by-dishIdes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItem>> multipleCreationWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                                       @Valid @RequestBody List<IdTo> dishIdes,
                                                                       @RequestParam @NonNull Integer restaurantId,
                                                                       @RequestParam @NonNull LocalDate date) {
        int userId = authUser.id();
        log.info("create menuItems from {} for {} by user {} with date{}", dishIdes, restaurantId, userId, date);
        restaurantRepository.getExistedAndBelonged(userId, restaurantId);
        dishIdes.forEach(dishId -> dishRepository.getExistedAndBelonged(userId, dishId.getId()));
        List<MenuItem> created = service.saveAll(dishIdes, date);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/filter?restaurantId={restaurantId}&date={date}")
                .buildAndExpand(restaurantId, date).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
