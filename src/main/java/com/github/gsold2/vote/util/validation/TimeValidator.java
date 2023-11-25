package com.github.gsold2.vote.util.validation;

import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.model.Vote;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TimeValidator {
    public static final int defaultLimitTimeForChange = 11;

    @Setter
    private int limitTimeForChange = defaultLimitTimeForChange;

    public void checkTime(Vote vote) {
        LocalTime localTime = LocalTime.now();
        if (localTime.isAfter(LocalTime.of(limitTimeForChange, 0))) {
            throw new IllegalRequestDataException(localTime + " is to later for voting");
        }
    }
}
