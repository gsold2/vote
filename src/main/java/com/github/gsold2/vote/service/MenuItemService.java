package com.github.gsold2.vote.service;

import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.repository.DishRepository;
import com.github.gsold2.vote.repository.MenuItemRepository;
import com.github.gsold2.vote.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(int dishId, int id) {
        MenuItem menuItem = menuItemRepository.getOrThrowNotFoundException(id);
        Dish dish = dishRepository.getOrThrowDataConflictException(dishId);
        if (!Objects.equals(menuItem.getRestaurantId(), dish.getRestaurant().getId())) {
            throw new IllegalRequestDataException("Dish id=" + dishId + " doesn't have restaurantId=" + menuItem.getRestaurantId());
        }
        menuItem.setDish(dish);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public MenuItem create(int dishId, MenuItem menuItem) {
        Dish existed = dishRepository.getOrThrowDataConflictException(dishId);
        menuItem.setDish(existed);
        menuItem.setRestaurantId(existed.getRestaurant().getId());
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public List<MenuItem> copyUpToday(Integer restaurantId, LocalDate date) {
        restaurantRepository.getOrThrowDataConflictException(restaurantId);
        if (menuItemRepository.getAllByRestaurantAndDate(restaurantId, LocalDate.now()).size() > 0) {
            throw new IllegalRequestDataException("Restaurant id=" + restaurantId + " already has menuItems up today. This method is not applicable.");
        }
        List<MenuItem> menuItems = menuItemRepository.getAllByRestaurantAndDate(restaurantId, date);
        List<MenuItem> clones = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            MenuItem clone = new MenuItem(null, LocalDate.now());
            clone.setDish(menuItem.getDish());
            clone.setRestaurantId(menuItem.getRestaurantId());
            clones.add(clone);
        }
        return menuItemRepository.saveAll(clones);
    }
}
