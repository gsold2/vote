package com.github.gsold2.vote.util;

import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.to.MenuItemTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenuItemUtil {
    public static List<MenuItemTo> getTos(Collection<MenuItem> menuItems) {
        return Converter.convert(menuItems, MenuItemUtil::createTo);
    }

    public static MenuItemTo createTo(MenuItem menuItem) {
        return new MenuItemTo(menuItem.getId(), menuItem.getDateOfMenu());
    }
}