package ru.bootjava.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.error.NotFoundException;
import ru.bootjava.vote.model.Dish;

import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurant.id = :restaurantId")
    Optional<Dish> get(int restaurantId, int id);

    default Dish getExistedOrBelonged(int restaurantId, int id) {
        return get(restaurantId, id).orElseThrow(
                () -> new NotFoundException("Dish id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}