package com.github.gsold2.vote.service;

import com.github.gsold2.vote.model.Restaurant;
import com.github.gsold2.vote.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void update(int id, String name) {
        Restaurant restaurant = restaurantRepository.getOrThrowNotFoundException(id);
        restaurant.setName(name);
        restaurantRepository.save(restaurant);
    }
}