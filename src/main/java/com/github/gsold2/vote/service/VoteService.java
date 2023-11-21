package com.github.gsold2.vote.service;

import com.github.gsold2.vote.model.Vote;
import com.github.gsold2.vote.repository.RestaurantRepository;
import com.github.gsold2.vote.repository.UserRepository;
import com.github.gsold2.vote.repository.VoteRepository;
import com.github.gsold2.vote.util.validation.DateTimeValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VoteService {
    private final DateTimeValidator dateTimeValidator;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public Vote save(int userId, int restaurantId, Vote vote) {
        vote.setUser(userRepository.getExisted(userId));
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return voteRepository.save(vote);
    }

    @Transactional
    public void update(int userId, int restaurantId, Vote vote) {
        dateTimeValidator.checkDateAndTime(vote);
        save(userId, restaurantId, vote);
    }

    public void delete(Vote vote) {
        dateTimeValidator.checkDateAndTime(vote);
        voteRepository.delete(vote);
    }
}