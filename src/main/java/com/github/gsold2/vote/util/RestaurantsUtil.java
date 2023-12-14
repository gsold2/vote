package com.github.gsold2.vote.util;

import com.github.gsold2.vote.model.Restaurant;
import com.github.gsold2.vote.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantsUtil {

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return Converter.convert(restaurants, RestaurantsUtil::createTo);
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenuItems());
    }
}