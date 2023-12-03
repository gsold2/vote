package com.github.gsold2.vote.repository;

import com.github.gsold2.vote.error.AppException;
import com.github.gsold2.vote.error.DataConflictException;
import com.github.gsold2.vote.error.IllegalRequestDataException;
import com.github.gsold2.vote.error.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions
    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id=:id")
    int delete(int id);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    /**
     * Return an entity or throw the exception with code 404. This is set in RestExceptionHandler.class.
     */
    default T getOrThrowNotFoundException(int id) {
        return getOrThrowAppException(id, new NotFoundException("Entity with id=" + id + " not found"));
    }

    /**
     * Return an entity or throw the exception with code 409. This is set in RestExceptionHandler.class.
     */
    default T getOrThrowDataConflictException(int id) {
        return getOrThrowAppException(id, new DataConflictException("Entity with id=" + id + " doesn't found"));
    }

    private T getOrThrowAppException(int id, AppException appException) {
        return findById(id).orElseThrow(() -> appException);
    }
}