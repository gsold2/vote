package ru.bootjava.vote.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuItemTo extends BaseTo {
    @NotNull
    LocalDate date;

    public MenuItemTo(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }
}
