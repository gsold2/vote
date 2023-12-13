package com.github.gsold2.vote.util.validation;

import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.model.Vote;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DateTimeValidator {

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    public void checkDateTime(Vote vote) {
        LocalDateTime localDateTime = LocalDateTime.now(clock);
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDate localDate = localDateTime.toLocalDate();
        if (localTime.isAfter(LocalTime.of(11, 0)) || dateIsNotMatched(vote, localDate)) {
            throw new IllegalRequestDataException("It's impossible to change your mind at " + localDateTime);
        }
    }

    private boolean dateIsNotMatched(Vote vote, LocalDate localDate) {
        return !vote.getVotingDate().isEqual(localDate);
    }
}
