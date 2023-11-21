package com.github.gsold2.vote.web.dish;

import com.github.gsold2.vote.model.Dish;
import com.github.gsold2.vote.to.DishTo;
import com.github.gsold2.vote.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final int DISH_ID = 1;
    public static final int NOT_EXISTED_DISH_ID = 10;

    public static final Dish dish1 = new Dish(DISH_ID, "dish_1", 100);
    public static final Dish dish2 = new Dish(DISH_ID + 1, "dish_2", 150);

    public static final List<Dish> dishes = List.of(dish1, dish2);

    public static Dish getNew() {
        return new Dish(null, "new_dish", 100);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_ID, "updated_dish", 150);
    }
}
