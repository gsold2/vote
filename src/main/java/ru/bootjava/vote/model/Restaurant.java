package ru.bootjava.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurant_idx", columnNames = {"user_id", "name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @Schema(hidden = true)
    public LocalTime getTime() {
        return getLocalDateTime().toLocalTime();
    }

    @Schema(hidden = true)
    public LocalDate getDate() {
        return getLocalDateTime().toLocalDate();
    }

    @Schema(hidden = true)
    private LocalDateTime getLocalDateTime() {
        return ZonedDateTime.now().toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
