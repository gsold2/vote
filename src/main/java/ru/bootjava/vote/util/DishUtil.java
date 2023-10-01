package ru.bootjava.vote.util;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.model.Dish;
import ru.bootjava.vote.to.DishTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class DishUtil extends ConvertingUtil {
    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return convert(dishes, DishUtil::createTo);
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPriceInCoins());
    }
}
