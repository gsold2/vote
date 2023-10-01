package ru.bootjava.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.error.DataConflictException;
import ru.bootjava.vote.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.user.id = :userId")
    List<Restaurant> getAll(int userId);

    @Query("SELECT r FROM Restaurant r JOIN MenuItem m ON r.id = m.dish.restaurant.id WHERE r.user.id = :userId AND m.date = :date")
    List<Restaurant> getAllWithMenuByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id and r.user.id = :userId")
    Optional<Restaurant> get(int userId, int id);

    default Restaurant getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}