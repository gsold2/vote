package ru.bootjava.vote.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.repository.DishRepository;
import ru.bootjava.vote.repository.MenuItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<MenuItem> saveAll(List<MenuItem> menuItems) {
        List<MenuItem> clones = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            MenuItem clone = new MenuItem(null, LocalDate.now());
            clone.setDish(menuItem.getDish());
            clones.add(clone);
        }
        return menuItemRepository.saveAllAndFlush(clones);
    }
}
