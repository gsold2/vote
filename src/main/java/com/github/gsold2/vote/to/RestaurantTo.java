package com.github.gsold2.vote.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}