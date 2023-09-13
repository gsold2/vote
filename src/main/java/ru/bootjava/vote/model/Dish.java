package ru.bootjava.vote.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(name = "dish_idx", columnNames = {"restaurant_id", "name", "price_in_coins"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "price_in_coins", nullable = false)
    @NotNull
    @Min(1)
    private Integer priceInCoins;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish(Integer id, String name, int priceInCoins) {
        super(id, name);
        this.priceInCoins = priceInCoins;
    }

    @Override
    public String toString() {
        return "Dish:" + id + '[' + name + ']';
    }
}
