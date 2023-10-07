package ru.bootjava.vote.web.vote;

import org.springframework.beans.factory.annotation.Autowired;
import ru.bootjava.vote.repository.VoteRepository;
import ru.bootjava.vote.web.AbstractControllerTest;

import static ru.bootjava.vote.web.vote.VoteController.REST_URL;

public class BaseVoteControllerTest extends AbstractControllerTest {

    static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    VoteRepository voteRepository;
}
