package com.github.gsold2.vote.to;

import com.github.gsold2.vote.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    private List<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, List<MenuItem> menuItems) {
        super(id, name);
        this.menuItems = menuItems;
    }
}