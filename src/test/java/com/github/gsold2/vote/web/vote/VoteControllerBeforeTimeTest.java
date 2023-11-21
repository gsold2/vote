package com.github.gsold2.vote.web.vote;

import com.github.gsold2.vote.model.Vote;
import com.github.gsold2.vote.util.validation.DateTimeValidator;
import com.github.gsold2.vote.web.user.UserTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.*;
import static com.github.gsold2.vote.web.user.UserTestData.ADMIN_MAIL;
import static com.github.gsold2.vote.web.vote.VoteTestData.getUpdated;
import static com.github.gsold2.vote.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerBeforeTimeTest extends BaseVoteControllerTest {

    @Autowired
    DateTimeValidator dateTimeValidator;

    @BeforeEach
    void beforeEach() {
        dateTimeValidator.setOffsetTime(LocalTime.now().getHour() + 1);
    }

    @AfterEach
    void afterEach() {
        dateTimeValidator.setOffsetTime(DateTimeValidator.defaultOffsetTime);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateValidTime() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Vote existed = voteRepository.getExisted(VOTE_ID + 2);
        VOTE_MATCHER.assertMatch(existed, updated);
        RESTAURANT_MATCHER.assertMatch(existed.getRestaurant(), restaurant1);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidDate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 1))
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_EXISTED_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(restaurant1.id())))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (VOTE_ID + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", String.valueOf(NOT_EXISTED_RESTAURANT_ID)))
                .andDo(print())
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