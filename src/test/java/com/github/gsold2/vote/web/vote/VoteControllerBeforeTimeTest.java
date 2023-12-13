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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.RESTAURANT_ID_MATCHER;
import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.gsold2.vote.web.user.UserTestData.ADMIN_MAIL;
import static com.github.gsold2.vote.web.vote.VoteController.REST_URL;
import static com.github.gsold2.vote.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerBeforeTimeTest extends BaseVoteControllerTest {

    @Autowired
    DateTimeValidator dateTimeValidator;

    @BeforeEach
    void beforeEach() {
        dateTimeValidator.setClock(Clock.fixed(Instant.parse(LocalDate.now() + "T10:00:00.00Z"), ZoneId.of("UTC")));
    }

    @AfterEach
    void afterEach() {
        dateTimeValidator.setClock(Clock.systemDefaultZone());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateValidTime() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Vote existed = voteRepository.getOrThrowNotFoundException(VOTE_ID + 2);
        Vote updated = getUpdated();
        VOTE_MATCHER.assertMatch(existed, updated);
        RESTAURANT_ID_MATCHER.assertMatch(existed.getRestaurant(), restaurant1);
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void setRestaurantNullValidTime() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + "delete-restaurantId"))
                .andExpect(status().isNoContent());
        assertNull(voteRepository.get(UserTestData.ADMIN_ID, (VOTE_ID + 2)).get().getRestaurant());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void setRestaurantNullValidTimeAndThenUpdate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + "delete-restaurantId"))
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Vote existed = voteRepository.getOrThrowNotFoundException(VOTE_ID + 2);
        Vote updated = getUpdated();
        VOTE_MATCHER.assertMatch(existed, updated);
        RESTAURANT_ID_MATCHER.assertMatch(existed.getRestaurant(), restaurant1);
    }
}
