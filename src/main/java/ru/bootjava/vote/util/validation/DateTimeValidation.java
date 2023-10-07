package ru.bootjava.vote.util.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bootjava.vote.error.IllegalRequestDataException;
import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.model.Vote;

import java.time.LocalTime;

@Component
public class DateTimeValidation {
    @Value("${offset_time}")
    private int offsetTime;

    public void checkTime(Restaurant restaurant) {
        if (restaurant.getTime().isAfter(LocalTime.of(offsetTime, 0))) {
            throw new IllegalRequestDataException(restaurant.getTime() + " is to later");
        }
    }

    public void checkDateAndTime(Restaurant restaurant, Vote vote) {
        checkTime(restaurant);
        if (!restaurant.getDate().equals(vote.getDate())) {
            throw new IllegalRequestDataException(vote.getDate() + " does not match the current date");
        }
    }
}
