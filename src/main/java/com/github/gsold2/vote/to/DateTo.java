package com.github.gsold2.vote.to;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class DateTo extends BaseTo {
    @NotNull
    LocalDate date;

    public DateTo(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }
}