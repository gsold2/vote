package ru.bootjava.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.bootjava.vote.util.validation.NoHtml;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurant_idx", columnNames = {"user_id", "name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    // Region-based ZoneId https://betacode.net/13715/java-zoneid & https://en.m.wikipedia.org/wiki/List_of_tz_database_time_zones
    @Column(name = "zone_id", nullable = false, columnDefinition = "varchar(255) default 'Europe/Moscow'")
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 128)
    @NoHtml
    private String zoneId;

    @Column(name = "offset_time", nullable = false, columnDefinition = "integer default 0")
    @NotNull
    @Range(min = -23, max = 24)
    private Integer offsetTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Restaurant(Integer id, String name) {
        this(id, name, "Europe/Moscow", 0);
    }

    public Restaurant(Integer id, String name, String zoneId, int offsetTime) {
        super(id, name);
        this.zoneId = zoneId;
        this.offsetTime = offsetTime;
    }

    public LocalTime getLocalTime() {
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalTime().plusHours(offsetTime);
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
