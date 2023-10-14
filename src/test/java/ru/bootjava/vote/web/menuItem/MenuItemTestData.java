package ru.bootjava.vote.web.menuItem;

import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.to.MenuItemTo;
import ru.bootjava.vote.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "restaurant", "dish");
    public static final MatcherFactory.Matcher<MenuItemTo> MENU_ITEM_TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuItemTo.class);

    public static final int MENU_ITEM_ID = 1;
    public static final int NOT_EXISTED_MENU_ITEM_ID = 20;

    public static final MenuItem menuItem1 = new MenuItem(MENU_ITEM_ID, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem2 = new MenuItem(MENU_ITEM_ID + 1, LocalDate.parse("2020-01-29"));
    public static final MenuItem menuItem5 = new MenuItem(MENU_ITEM_ID + 4, LocalDate.now());

    public static final List<MenuItem> menuItems = List.of(menuItem1, menuItem2);

    public static MenuItem getNew() {
        return new MenuItem(null, LocalDate.parse("2020-02-01"));
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID, LocalDate.parse("2020-02-01"));
    }
}
