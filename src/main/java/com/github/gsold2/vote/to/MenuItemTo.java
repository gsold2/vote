package com.github.gsold2.vote.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuItemTo extends DateTo {
    public MenuItemTo(Integer id, LocalDate date) {
        super(id, date);
    }
}
