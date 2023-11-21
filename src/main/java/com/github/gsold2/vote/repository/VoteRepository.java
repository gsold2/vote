package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.DataConflictException;
import com.github.gsold2.vote.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId")
    List<Vote> getAll(int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.date >= :startDate AND v.date <= :endDate")
    List<Vote> getBetweenWithInclusion(int userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.id = :id AND v.user.id = :userId")
    Optional<Vote> get(int userId, int id);

    default Vote getExistedAndBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("Vote id=" + id + " is not exist or doesn't belong to User id=" + userId));
    }
}
