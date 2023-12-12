package com.github.gsold2.vote.util;

import com.github.gsold2.vote.model.Vote;
import com.github.gsold2.vote.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return Converter.convert(votes, VoteUtil::createTo);
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVotingDate());
    }
}
