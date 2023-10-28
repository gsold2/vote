package ru.bootjava.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(name = "dish_idx", columnNames = {"restaurant_id", "name", "price"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(0)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']';
    }
}
