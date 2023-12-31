package com.github.gsold2.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(name = "restaurant_idx", columnNames = {"name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY)//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("dateOfMenu DESC")
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private List<MenuItem> menuItems;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
