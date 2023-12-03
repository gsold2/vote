package com.github.gsold2.vote.service;

import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.repository.DishRepository;
import com.github.gsold2.vote.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Dish create(int restaurantId, Dish dish) {
        dish.setRestaurant(restaurantRepository.getOrThrowDataConflictException(restaurantId));
        return dishRepository.save(dish);
    }

    @Transactional
    public void update(int id, Dish dish) {
        Dish existed = dishRepository.getOrThrowNotFoundException(id);
        existed.setPrice(dish.getPrice());
        existed.setName(dish.getName());
        dishRepository.save(existed);
    }
}
