package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.DataConflictException;
import com.github.gsold2.vote.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.user.id = :userId")
    List<Restaurant> getAll(int userId);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menuItems mi WHERE mi.dateOfMenu = CURRENT_DATE AND r.id = mi.dish.restaurant.id")
    List<Restaurant> getAllWithMenuUpToday();

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id AND r.user.id = :userId")
    Optional<Restaurant> get(int userId, int id);

    default Restaurant getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}