package ru.bootjava.vote.web.menuItem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.vote.model.MenuItem;
import ru.bootjava.vote.repository.MenuItemRepository;
import ru.bootjava.vote.util.JsonUtil;
import ru.bootjava.vote.util.MenuItemUtil;
import ru.bootjava.vote.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.vote.web.dish.DishTestData.dish1;
import static ru.bootjava.vote.web.dish.DishTestData.dish2;
import static ru.bootjava.vote.web.menuItem.MenuItemController.REST_URL;
import static ru.bootjava.vote.web.menuItem.MenuItemTestData.*;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant1;
import static ru.bootjava.vote.web.restaurant.RestaurantTestData.restaurant2;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_ID;
import static ru.bootjava.vote.web.user.UserTestData.ADMIN_MAIL;

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
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + MENU_ITEM_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.get(ADMIN_ID, MENU_ITEM_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_EXISTED_MENU_ITEM_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU_ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getExisted(MENU_ITEM_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(dish2.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)));

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getExisted(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "filter")
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .param("date", String.valueOf(menuItem1.getDate())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(MenuItemUtil.getTos(menuItems)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuItem invalid = new MenuItem(null, null);
        perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(dish1.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuItem invalid = new MenuItem(MENU_ITEM_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU_ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        MenuItem invalid = new MenuItem(MENU_ITEM_ID, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU_ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuItem invalid = new MenuItem(null, menuItem1.getDate());
        perform(MockMvcRequestBuilders.post(MenuItemController.REST_URL)
                .param("dishId", String.valueOf(dish1.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void multipleCreationWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "/copy-up-today")
                .param("restaurantId", String.valueOf(restaurant2.id()))
                .param("date", "2020-01-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        List<MenuItem> created = MENU_ITEM_MATCHER.readListFromJson(action);
        int newId = created.get(0).id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, List.of(newMenuItem));
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getExisted(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void multipleCreationMenuIsNotEmpty() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "/copy-up-today")
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .param("date", String.valueOf(LocalDate.now()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
