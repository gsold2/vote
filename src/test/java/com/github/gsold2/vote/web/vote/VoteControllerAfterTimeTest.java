package com.github.gsold2.vote.web.vote;

import com.github.gsold2.vote.util.validation.DateTimeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.gsold2.vote.web.user.UserTestData.ADMIN_MAIL;
import static com.github.gsold2.vote.web.vote.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerAfterTimeTest extends BaseVoteControllerTest {

    @Autowired
    DateTimeValidator dateTimeValidator;

    @BeforeEach
    void beforeEach() {
        dateTimeValidator.setOffsetTime(LocalTime.now().getHour());
    }

    @AfterEach
    void afterEach() {
        dateTimeValidator.setOffsetTime(DateTimeValidator.defaultOffsetTime);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalidTime() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void setRestaurantNullInvalidTime() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + "delete-restaurantId"))
                .andExpect(status().isUnprocessableEntity());
    }
}
