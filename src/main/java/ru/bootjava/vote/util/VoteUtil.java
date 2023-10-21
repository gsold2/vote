package ru.bootjava.vote.util;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.to.VoteTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return Converter.convert(votes, VoteUtil::createTo);
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDate());
    }
}
