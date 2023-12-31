package com.github.gsold2.vote.web.menu;

import com.github.gsold2.vote.model.MenuItem;
import com.github.gsold2.vote.repository.MenuItemRepository;
import com.github.gsold2.vote.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.gsold2.vote.web.dish.DishTestData.*;
import static com.github.gsold2.vote.web.menu.MenuItemController.REST_URL;
import static com.github.gsold2.vote.web.menu.MenuItemTestData.getNew;
import static com.github.gsold2.vote.web.menu.MenuItemTestData.getUpdated;
import static com.github.gsold2.vote.web.menu.MenuItemTestData.*;
import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.gsold2.vote.web.restaurant.RestaurantTestData.restaurant2;
import static com.github.gsold2.vote.web.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuItemControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU_ITEM_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItem1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU_ITEM_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_EXISTED_MENU_ITEM_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "filter")
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .param("date", String.valueOf(menuItem1.getDateOfMenu())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItems));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + MENU_ITEM_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.findById(MENU_ITEM_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_EXISTED_MENU_ITEM_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (MENU_ITEM_ID + 3))
                .param("dishId", String.valueOf(dish2.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        MenuItem existed = menuItemRepository.getOrThrowNotFoundException(MENU_ITEM_ID + 3);
        MENU_ITEM_MATCHER.assertMatch(existed, updated);
        DISH_MATCHER.assertMatch(existed.getDish(), dish2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_EXISTED_MENU_ITEM_ID)
                .param("dishId", String.valueOf(dish2.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + (MENU_ITEM_ID + 3))
                .param("dishId", String.valueOf(NOT_EXISTED_DISH_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU_ITEM_ID)
                .param("dishId", String.valueOf(dish2.id()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateIsNotConsistent() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU_ITEM_ID)
                .param("dishId", String.valueOf(DISH_ID + 2))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(dish2.id()))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON));

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getOrThrowNotFoundException(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL + NOT_EXISTED_MENU_ITEM_ID)
                .param("dishId", String.valueOf(dish2.id()))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(NOT_EXISTED_DISH_ID))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(dish1.id()))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void multipleCreationWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "copy-up-today")
                .param("restaurantId", String.valueOf(restaurant2.id()))
                .param("date", "2020-01-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        List<MenuItem> created = MENU_ITEM_MATCHER.readListFromJson(action);
        int newId = created.get(0).id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, List.of(newMenuItem));
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getOrThrowNotFoundException(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void multipleCreationMenuIsNotEmpty() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "copy-up-today")
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
