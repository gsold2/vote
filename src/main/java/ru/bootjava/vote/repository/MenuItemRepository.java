package ru.bootjava.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.error.DataConflictException;
import ru.bootjava.vote.error.NotFoundException;
import ru.bootjava.vote.model.Dish;
import ru.bootjava.vote.model.MenuItem;

import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m WHERE m.id = :id and m.dish.id = :dishId")
    Optional<MenuItem> get(int dishId, int id);

    default MenuItem getExistedOrBelonged(int dishId, int id) {
        return get(dishId, id).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " is not exist or doesn't belong to Dish id=" + dishId));
    }
}