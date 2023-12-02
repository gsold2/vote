package com.github.gsold2.vote.web.menuItem;

import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.model.Restaurant;
import com.github.gsold2.vote.repository.DishRepository;
import com.github.gsold2.vote.repository.MenuItemRepository;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.service.MenuItemService;
import com.github.gsold2.vote.web.AuthUser;
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
import java.time.LocalDate;
import java.util.List;

import static com.github.gsold2.vote.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "menuItems")
public class MenuItemController {
    static final String REST_URL = "/api/admin/menu-items";

    private final MenuItemRepository menuItemRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemService service;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get menu item {} for user {}", id, authUser.id());
        return ResponseEntity.of(menuItemRepository.get(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        MenuItem menuItem = menuItemRepository.getIfExisted(id);
        menuItemRepository.delete(menuItem);
    }

    @GetMapping("/filter")
    @Cacheable(key = "{#restaurantId, #date}")
    public List<MenuItem> getAllByRestaurantAndDate(@AuthenticationPrincipal AuthUser authUser,
                                                    @RequestParam int restaurantId,
                                                    @RequestParam LocalDate date) {
        log.info("get all menu items for the user {} and restaurant{} on date {}", authUser.id(), restaurantId, date);
        return menuItemRepository.getAllByRestaurantAndDate(restaurantId, date);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int dishId, @PathVariable int id) {
        int userId = authUser.id();
        MenuItem menuItem = menuItemRepository.getIfExisted(id);
        log.info("update {} for user {}", menuItem, userId);
        Restaurant restaurant = menuItem.getDish().getRestaurant();
        int restaurantIdFromDish = dishRepository.getIfExisted(dishId).getRestaurant().id();
        assureIdConsistent(restaurant, restaurantIdFromDish);
        service.save(dishId, menuItem);
    }

    @PostMapping()
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                       @RequestParam LocalDate date,
                                                       @RequestParam int dishId) {
        int userId = authUser.id();
        MenuItem menuItem = new MenuItem(null, date);
        log.info("create {} for dish {} by user {}", menuItem, dishId, userId);
        dishRepository.getIfExisted(dishId);
        MenuItem created = service.save(dishId, menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/copy-up-today")
    @CacheEvict(allEntries = true)
    public ResponseEntity<List<MenuItem>> multipleCreationWithLocationUpToday(@AuthenticationPrincipal AuthUser authUser,
                                                                              @RequestParam Integer restaurantId,
                                                                              @RequestParam LocalDate date) {
        log.info("clone menu items for {} with date {}", restaurantId, date);
        restaurantRepository.getIfExisted(restaurantId);
        List<MenuItem> created = service.copyUpToday(restaurantId, date);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/filter?restaurantId={restaurantId}&date={date}")
                .buildAndExpand(restaurantId, LocalDate.now()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
