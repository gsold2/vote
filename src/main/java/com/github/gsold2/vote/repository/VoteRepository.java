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

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.votingDate >= :startDate AND v.votingDate <= :endDate")
    List<Vote> getBetweenWithInclusion(int userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.restaurant WHERE v.id = :id AND v.user.id = :userId")
    Optional<Vote> get(int userId, int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.votingDate = CURRENT_DATE")
    Optional<Vote> getUpToday(int userId);

    default Vote getExistedUpToday(int userId) {
        return getUpToday(userId).orElseThrow(
                () -> new DataConflictException("User id=" + userId + " doesn't vote today"));
    }
}
