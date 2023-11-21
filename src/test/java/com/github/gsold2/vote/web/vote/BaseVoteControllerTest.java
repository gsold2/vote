package com.github.gsold2.vote.web.vote;

import com.github.gsold2.vote.repository.VoteRepository;
import com.github.gsold2.vote.web.AbstractControllerTest;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.gsold2.vote.web.vote.VoteController.REST_URL;

public class BaseVoteControllerTest extends AbstractControllerTest {

    static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    VoteRepository voteRepository;
}
