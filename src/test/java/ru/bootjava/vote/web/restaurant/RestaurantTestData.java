package ru.bootjava.vote.web.restaurant;

import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.to.RestaurantTo;
import ru.bootjava.vote.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =  MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);

    public static final int RESTAURANT_ID = 1;
    public static final int NOT_EXISTED_RESTAURANT_ID = 10;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID, "restaurant_1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID + 1, "restaurant_2");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2);

    public static Restaurant getNew() {
        return new Restaurant(null, "new_restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID, "updated_restaurant");
    }
}