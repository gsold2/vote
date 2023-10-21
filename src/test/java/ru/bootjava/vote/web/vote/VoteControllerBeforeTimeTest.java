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
import ru.bootjava.vote.web.user.UserTestData;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.vote.web.vote.VoteTestData.*;

public class VoteControllerBeforeTimeTest extends BaseVoteControllerTest {

    @Autowired
    DateTimeValidation dateTimeValidation;

    @BeforeEach
    void beforeEach() {
        dateTimeValidation.setOffsetTime(LocalTime.now().getHour() + 1);
    }

    @AfterEach
    void afterEach() {
        dateTimeValidation.setOffsetTime(DateTimeValidation.defaultOffsetTime);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateValidTime() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteRepository.getExisted(VOTE_ID + 2), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidDate() throws Exception {
        Vote invalid = getUpdated();
        invalid.setDate(LocalDate.parse("2020-01-30"));
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidRestaurant() throws Exception {
        Vote invalid = getUpdated();
        invalid.setRestaurant(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteValidTime() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + (VOTE_ID + 2)))
                .andExpect(status().isNoContent());
        assertFalse(voteRepository.get(UserTestData.ADMIN_ID, (VOTE_ID + 2)).isPresent());
    }
}
