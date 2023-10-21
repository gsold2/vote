package ru.bootjava.vote.web.vote;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.util.JsonUtil;
import ru.bootjava.vote.util.validation.DateTimeValidation;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.vote.web.vote.VoteTestData.VOTE_ID;
import static ru.bootjava.vote.web.vote.VoteTestData.getUpdated;

public class VoteControllerAfterTimeTest extends BaseVoteControllerTest {

    @Autowired
    DateTimeValidation dateTimeValidation;

    @BeforeEach
    void beforeEach() {
        dateTimeValidation.setOffsetTime(LocalTime.now().getHour());
    }

    @AfterEach
    void afterEach() {
        dateTimeValidation.setOffsetTime(DateTimeValidation.defaultOffsetTime);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidTime() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteInvalidTime() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + (VOTE_ID + 2)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteInvalidDay() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + (VOTE_ID + 1)))
                .andExpect(status().isUnprocessableEntity());
    }
}
