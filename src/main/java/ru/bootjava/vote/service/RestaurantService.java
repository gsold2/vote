package ru.bootjava.vote.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.repository.UserRepository;

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