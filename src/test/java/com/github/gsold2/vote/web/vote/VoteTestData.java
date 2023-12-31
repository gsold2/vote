package com.github.gsold2.vote.web.vote;


import com.github.gsold2.vote.model.Vote;
import com.github.gsold2.vote.to.VoteTo;
import com.github.gsold2.vote.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user");
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int VOTE_ID = 1;
    public static final int NOT_EXISTED_VOTE_ID = 10;

    public static final Vote vote1 = new Vote(VOTE_ID, LocalDate.parse("2020-01-30"));
    public static final Vote vote2 = new Vote(VOTE_ID + 1, LocalDate.parse("2020-01-30"));
    public static final Vote vote3 = new Vote(VOTE_ID + 2, LocalDate.now());

    public static final List<Vote> votes = List.of(vote2, vote3);

    public static Vote getNew() {
        return new Vote(null, getDate());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_ID + 2, getDate());
    }

    private static LocalDate getDate() {
        return LocalDate.now();
    }
}
