package ru.bootjava.vote.web.menuItem;

import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.to.MenuItemTo;
import ru.bootjava.vote.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingEqualsComparator(MenuItem.class);
    public static MatcherFactory.Matcher<MenuItemTo> MENU_ITEM_TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuItemTo.class);

    public static final int MENU_ITEM_ID = 1;
    public static final int NOT_EXISTED_MENU_ITEM_ID = 20;

    public static final MenuItem menuItem1 = new MenuItem(MENU_ITEM_ID, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem2 = new MenuItem(MENU_ITEM_ID + 1, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem3 = new MenuItem(MENU_ITEM_ID + 2, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem4 = new MenuItem(MENU_ITEM_ID + 3, LocalDate.parse("2020-01-30"));
    public static final MenuItem menuItem5 = new MenuItem(MENU_ITEM_ID + 4, LocalDate.parse("2023-01-29"));
    public static final MenuItem menuItem6 = new MenuItem(MENU_ITEM_ID + 5, LocalDate.parse("2023-01-29"));
    public static final MenuItem menuItem7 = new MenuItem(MENU_ITEM_ID + 6, LocalDate.parse("2023-01-29"));

    public static final List<MenuItem> menu_items_1 = List.of(menuItem1, menuItem2, menuItem3);
    public static final List<MenuItem> menu_items_2 = List.of(menuItem5, menuItem6, menuItem7);

    public static MenuItem getNew() {
        return new MenuItem(null, LocalDate.parse("2020-02-01"));
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID, LocalDate.parse("2020-02-01"));
    }
}
