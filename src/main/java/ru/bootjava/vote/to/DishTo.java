package ru.bootjava.vote.to;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {
    @NotNull
    @Min(0)
    Integer priceInCoins;

    public DishTo(Integer id, String name, int priceInCoins) {
        super(id, name);
        this.priceInCoins = priceInCoins;
    }
}
