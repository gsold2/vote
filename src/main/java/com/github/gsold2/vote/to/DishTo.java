package com.github.gsold2.vote.to;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {
    @NotNull
    @Min(0)
    Integer price;

    public DishTo(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }
}
