package ru.bootjava.vote.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.repository.DishRepository;
import ru.bootjava.vote.repository.MenuItemRepository;
import ru.bootjava.vote.to.IdTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final DishRepository dishRepository;

    @Transactional
    public MenuItem save(int dishId, MenuItem menuItem) {
        menuItem.setDish(dishRepository.getExisted(dishId));
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public List<MenuItem> saveAll(List<IdTo> dishIdes, LocalDate date) {
        List<MenuItem> menuItems = new ArrayList<>();
        for (IdTo dishId : dishIdes) {
            MenuItem menuItem = new MenuItem(null, date);
            menuItem.setDish(dishRepository.getExisted(dishId.id()));
            menuItems.add(menuItem);
        }
        return menuItemRepository.saveAllAndFlush(menuItems);
    }
}