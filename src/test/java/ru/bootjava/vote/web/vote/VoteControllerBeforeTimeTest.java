package ru.bootjava.vote.web.vote;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.util.validation.DateTimeValidation;
import ru.bootjava.vote.web.user.UserTestData;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.NOT_EXISTED_RESTAURANT_ID;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant1;
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
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteRepository.getExisted(VOTE_ID + 2), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidDate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 1))
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_EXISTED_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(restaurant1.id())))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(NOT_EXISTED_RESTAURANT_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteValidTime() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + (VOTE_ID + 2)))
                .andExpect(status().isNoContent());
        assertFalse(voteRepository.get(UserTestData.ADMIN_ID, (VOTE_ID + 2)).isPresent());
    }
}
