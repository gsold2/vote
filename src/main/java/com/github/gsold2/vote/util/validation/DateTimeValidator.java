package com.github.gsold2.vote.util.validation;

import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.model.Vote;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DateTimeValidator {
    public static final int defaultOffsetTime = 11;

    private int offsetTime = defaultOffsetTime;

    public void checkDateAndTime(Vote vote) {
        if (!LocalDate.now().equals(vote.getDate())) {
            throw new IllegalRequestDataException(vote.getDate() + " does not match the current date");
        }
        LocalTime localTime = LocalTime.now();
        if (localTime.isAfter(LocalTime.of(offsetTime, 0))) {
            throw new IllegalRequestDataException(localTime + " is to later for voting");
        }
    }

    public void setOffsetTime(int offsetTime) {
        this.offsetTime = offsetTime;
    }
}
