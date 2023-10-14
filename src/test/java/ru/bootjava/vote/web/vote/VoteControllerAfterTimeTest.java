package ru.bootjava.vote.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.vote.model.Vote;
import ru.bootjava.vote.util.JsonUtil;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant1;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.vote.web.vote.VoteTestData.VOTE_ID;
import static ru.bootjava.vote.web.vote.VoteTestData.getUpdated;

public class VoteControllerAfterTimeTest extends BaseVoteControllerTest {

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        int offsetTime = ZonedDateTime.now().toLocalTime().getHour();
        registry.add("offset_time", () -> offsetTime);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalidTime() throws Exception {
        perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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
}
