package ru.bootjava.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.error.DataConflictException;
import ru.bootjava.vote.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.user.id = :userId AND d.restaurant.id=:restaurantId")
    List<Dish> getAllByRestaurant(@Param("userId") int userId, @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id = :id AND d.restaurant.user.id = :userId")
    Optional<Dish> get(int userId, int id);

    default Dish getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}