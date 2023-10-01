package ru.bootjava.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.error.DataConflictException;
import ru.bootjava.vote.model.MenuItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish WHERE m.dish.restaurant.user.id = :userId AND m.dish.restaurant.id=:restaurantId AND m.date = :date")
    List<MenuItem> getAllByRestaurantAndDate(@Param("userId") int userId, @Param("restaurantId") int restaurantId, @Param("date") LocalDate date);

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish WHERE m.dish.restaurant.user.id = :userId AND m.id = :id")
    Optional<MenuItem> get(int userId, int id);

    default MenuItem getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}