package ru.bootjava.vote.web.dish;

import ru.bootjava.vote.model.Dish;
import ru.bootjava.vote.to.DishTo;
import ru.bootjava.vote.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);
    public static MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final int DISH_ID = 1;
    public static final int NOT_EXISTED_DISH_ID = 10;

    public static final Dish dish1 = new Dish(DISH_ID, "dish_1", 100);
    public static final Dish dish2 = new Dish(DISH_ID + 1, "dish_2", 150);
    public static final Dish dish3 = new Dish(DISH_ID + 2, "dish_3", 200);
    public static final Dish dish4 = new Dish(DISH_ID + 3, "dish_1", 100);
    public static final Dish dish5 = new Dish(DISH_ID + 4, "dish_2", 150);
    public static final Dish dish6 = new Dish(DISH_ID + 5, "dish_3", 200);

    public static final List<Dish> dishes1 = List.of(dish1, dish2, dish3);
    public static final List<Dish> dishes2 = List.of(dish4, dish5, dish6);
    public static final List<Dish> allDishes = List.of(dish1, dish2, dish3, dish4, dish5, dish6);

    public static Dish getNew() {
        return new Dish(null, "new_dish", 100);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_ID, "updated_dish", 150);
    }
}
