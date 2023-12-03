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

    @Query("SELECT m FROM MenuItem m WHERE m.dateOfMenu = :date AND m.dish.restaurant.id = :restaurantId")
    List<MenuItem> getAllByRestaurantAndDate(int restaurantId, LocalDate date);
}