package com.github.gsold2.vote.util;

import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.to.DishTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class DishUtil {
    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return Converter.convert(dishes, DishUtil::createTo);
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }
}
