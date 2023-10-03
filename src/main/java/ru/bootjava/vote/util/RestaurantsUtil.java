package ru.bootjava.vote.util;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantsUtil {

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return Converter.convert(restaurants, RestaurantsUtil::createTo);
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }
}