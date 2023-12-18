package com.github.gsold2.vote.web.menu;

import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.repository.MenuItemRepository;
import com.github.gsold2.vote.service.MenuItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuItemController {
    static final String REST_URL = "/api/admin/menu-items";

    private final MenuItemRepository menuItemRepository;
    private final MenuItemService service;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> get(@PathVariable int id) {
        log.info("get menu item {}", id);
        return ResponseEntity.of(menuItemRepository.get(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "restaurants", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        menuItemRepository.deleteExisted(id);
    }

    @GetMapping("/filter")
    public List<MenuItem> getAllByRestaurantAndDate(@RequestParam int restaurantId,
                                                    @RequestParam LocalDate date) {
        log.info("get all menu items for restaurant{} on date {}", restaurantId, date);
        return menuItemRepository.getAllByRestaurantAndDate(restaurantId, date);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam int dishId, @PathVariable int id) {
        log.info("update {}", id);
        service.update(dishId, id);
    }

    @PostMapping()
    public ResponseEntity<MenuItem> createWithLocation(@RequestParam LocalDate date,
                                                       @RequestParam int dishId) {
        MenuItem menuItem = new MenuItem(null, date);
        log.info("create {} for dish {}", menuItem, dishId);
        MenuItem created = service.create(dishId, menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/copy-up-today")
    public ResponseEntity<List<MenuItem>> multipleCreationWithLocationUpToday(@RequestParam Integer restaurantId,
                                                                              @RequestParam LocalDate date) {
        log.info("clone menu items for {} with date {}", restaurantId, date);
        List<MenuItem> created = service.copyUpToday(restaurantId, date);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/filter?restaurantId={restaurantId}&date={date}")
                .buildAndExpand(restaurantId, LocalDate.now()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
