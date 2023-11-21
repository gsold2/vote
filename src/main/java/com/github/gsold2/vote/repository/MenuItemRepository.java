package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.DataConflictException;
import com.github.gsold2.vote.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish JOIN FETCH m.dish.restaurant WHERE m.date = :date AND m.dish.restaurant.user.id = :userId AND m.dish.restaurant.id=:restaurantId")
    List<MenuItem> getAllByRestaurantAndDate(int userId, int restaurantId, LocalDate date);

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish JOIN FETCH m.dish.restaurant WHERE m.dish.restaurant.user.id = :userId AND m.id = :id")
    Optional<MenuItem> get(int userId, int id);

    default MenuItem getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}