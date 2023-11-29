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
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(name = "vote_idx", columnNames = {"user_id", "voting_date"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "voting_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date votingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Vote(Integer id, LocalDate votingDate) {
        super(id);
        this.votingDate = Date.valueOf(votingDate);
    }

    @Override
    public String toString() {
        return "Vote:" + id + '[' + votingDate + ']';
    }
}
