package com.github.gsold2.vote.web.menuItem;

import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "dish", "restaurantId");

    public static final int MENU_ITEM_ID = 1;
    public static final int NOT_EXISTED_MENU_ITEM_ID = 20;

    public static final MenuItem menuItem1 = new MenuItem(MENU_ITEM_ID, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem2 = new MenuItem(MENU_ITEM_ID + 1, LocalDate.parse("2020-01-29"));

    public static final List<MenuItem> menuItems = List.of(menuItem1, menuItem2);

    public static MenuItem getNew() {
        return new MenuItem(null, LocalDate.now());
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID + 3, LocalDate.now());
    }
}
