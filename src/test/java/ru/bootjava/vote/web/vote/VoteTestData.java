package ru.bootjava.vote.web.vote;


import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.to.VoteTo;
import ru.bootjava.vote.web.MatcherFactory;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant2;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int VOTE_ID = 1;
    public static final int NOT_EXISTED_VOTE_ID = 10;

    public static final Vote vote1 = new Vote(VOTE_ID, LocalDate.parse("2020-01-30"));
    public static final Vote vote2 = new Vote(VOTE_ID + 1, LocalDate.parse("2020-01-30"));
    public static final Vote vote3 = new Vote(VOTE_ID + 2, LocalDate.now());

    public static final List<Vote> votes1 = List.of(vote1, vote2, vote3);

    public static Vote getNew() {
        return new Vote(null, getDate());
    }

    public static Vote getUpdated() {
        Vote vote = new Vote(VOTE_ID + 2, getDate());
        vote.setRestaurant(restaurant2);
        return vote;
    }

    private static LocalDate getDate() {
        return ZonedDateTime.now().toLocalDateTime().toLocalDate();
    }
}
