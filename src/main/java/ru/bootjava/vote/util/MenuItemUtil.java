package ru.bootjava.vote.util;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.to.MenuItemTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenuItemUtil extends ConvertingUtil {
    public static List<MenuItemTo> getTos(Collection<MenuItem> menuItems) {
        return convert(menuItems, MenuItemUtil::createTo);
    }

    public static MenuItemTo createTo(MenuItem menuItem) {
        return new MenuItemTo(menuItem.getId(), menuItem.getDate());
    }
}
