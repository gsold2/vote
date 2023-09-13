package ru.bootjava.vote.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(name = "menu_idx", columnNames = {"dish_id", "date"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends BaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dish dish;

    public MenuItem(Integer id, Date date) {
        super(id);
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuItem:" + id + '[' + dish + ']';
    }
}
