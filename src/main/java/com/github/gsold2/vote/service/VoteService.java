package com.github.gsold2.vote.service;

import com.github.gsold2.vote.model.User;
import com.github.gsold2.vote.model.Vote;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.repository.UserRepository;
import com.github.gsold2.vote.repository.VoteRepository;
import com.github.gsold2.vote.util.validation.TimeValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VoteService {
    private final TimeValidator timeValidator;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public Vote save(User user, int restaurantId, Vote vote) {
        vote.setUser(user);
        vote.setRestaurant(restaurantRepository.getOrThrowIllegalRequestDataException(restaurantId));
        return voteRepository.save(vote);
    }

    @Transactional
    public void update(int restaurantId, Vote vote) {
        timeValidator.checkTime(vote);
        vote.setRestaurant(restaurantRepository.getOrThrowIllegalRequestDataException(restaurantId));
        voteRepository.save(vote);
    }

    @Transactional
    public void setRestaurantNull(Vote vote) {
        timeValidator.checkTime(vote);
        vote.setRestaurant(null);
        voteRepository.save(vote);
    }
}
