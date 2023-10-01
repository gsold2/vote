package ru.bootjava.vote.util;

import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.to.MenuItemTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MenuItemUtil {
    public static List<MenuItemTo> getTos(Collection<MenuItem> menuItems) {
        return menuItems.stream()
                .map(MenuItemUtil::createTo)
                .collect(Collectors.toList());
    }

    public static MenuItemTo createTo(MenuItem menuItem) {
        return new MenuItemTo(menuItem.getId(), menuItem.getDate());
    }
}
