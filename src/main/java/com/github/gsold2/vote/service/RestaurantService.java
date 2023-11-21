package com.github.gsold2.vote.service;

import com.github.gsold2.vote.model.Restaurant;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public Restaurant save(int userId, Restaurant restaurant) {
        restaurant.setUser(userRepository.getExisted(userId));
        return restaurantRepository.save(restaurant);
    }
}