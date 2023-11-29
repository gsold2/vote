package com.github.gsold2.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(name = "menu_idx", columnNames = {"dish_id", "date_of_menu"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends BaseEntity {

    @Column(name = "date_of_menu", nullable = false)
    @NotNull
    private Date dateOfMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    @JsonIgnore
    private Integer restaurantId;

    public MenuItem(Integer id, LocalDate dateOfMenu) {
        super(id);
        this.dateOfMenu = Date.valueOf(dateOfMenu);
    }

    @Override
    public String toString() {
        return "MenuItem:" + id + '[' + dish + ']';
    }
}
