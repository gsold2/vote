package com.github.gsold2.vote.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends DateTo {
    public VoteTo(Integer id, LocalDate date) {
        super(id, date);
    }
}