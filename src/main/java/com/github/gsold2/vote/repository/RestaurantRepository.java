package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> getAll();

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menuItems mi WHERE mi.dateOfMenu = CURRENT_DATE")
    List<Restaurant> getAllWithMenuUpToday();

    /**
     * Return a restaurant or throw the exception with code 422. This is set in RestExceptionHandler.class.
     */
    default Restaurant getOrThrowIllegalRequestDataException(int id) {
        return findById(id).orElseThrow(() -> new IllegalRequestDataException("Restaurant with id=" + id + " doesn't found"));
    }
}