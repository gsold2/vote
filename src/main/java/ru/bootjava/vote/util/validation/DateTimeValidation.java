package ru.bootjava.vote.util.validation;

import org.springframework.stereotype.Component;
import ru.bootjava.vote.error.IllegalRequestDataException;
import ru.bootjava.vote.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DateTimeValidation {
    public static final int defaultOffsetTime = 11;

    private int offsetTime = defaultOffsetTime;

    public void checkTime() {
        LocalTime localTime = LocalTime.now();
        if (localTime.isAfter(LocalTime.of(offsetTime, 0))) {
            throw new IllegalRequestDataException(localTime + " is to later for voting");
        }
    }

    public void checkDateAndTime(Vote vote) {
        checkTime();
        if (!LocalDate.now().equals(vote.getDate())) {
            throw new IllegalRequestDataException(vote.getDate() + " does not match the current date");
        }
    }

    public void setOffsetTime(int offsetTime) {
        this.offsetTime = offsetTime;
    }
}
