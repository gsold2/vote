package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.DataConflictException;
import com.github.gsold2.vote.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId")
    List<Dish> getAllByRestaurant(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id")
    Optional<Dish> get(int id);

    default Dish getIfExisted(int id) {
        return get(id).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " is not existed"));
    }
}