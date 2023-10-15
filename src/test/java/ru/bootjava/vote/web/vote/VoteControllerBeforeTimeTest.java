package ru.bootjava.vote.web.vote;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.util.JsonUtil;
import ru.bootjava.vote.util.validation.DateTimeValidation;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant1;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.vote.web.user.UserTestData.USER_MAIL;
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
    @WithUserDetails(value = USER_MAIL)
    void createValidTime() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
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
}
