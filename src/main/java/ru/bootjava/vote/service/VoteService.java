package ru.bootjava.vote.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.repository.RestaurantRepository;
import ru.bootjava.vote.repository.UserRepository;
import ru.bootjava.vote.repository.VoteRepository;
import ru.bootjava.vote.util.validation.DateTimeValidator;

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
