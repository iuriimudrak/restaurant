package ru.iuriimudrak.restaurant.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iuriimudrak.restaurant.model.Dish;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.service.DishService;
import ru.iuriimudrak.restaurant.service.RestaurantService;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;
import ru.iuriimudrak.restaurant.web.AbstractControllerTest;
import ru.iuriimudrak.restaurant.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iuriimudrak.restaurant.DishTestData.*;
import static ru.iuriimudrak.restaurant.RestaurantTestData.*;
import static ru.iuriimudrak.restaurant.TestUtil.readFromJson;
import static ru.iuriimudrak.restaurant.TestUtil.userHttpBasic;
import static ru.iuriimudrak.restaurant.UserTestData.ADMIN;

class AdminRestaurantRestControllerTest extends AbstractControllerTest {

	private static final String REST_URL = AdminRestaurantRestController.REST_URL + '/';
	private static final String DISHES_REST_URL = AdminRestaurantRestController.REST_URL + AdminRestaurantRestController.DISH_URL + '/';

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	DishService dishService;

	@Test
	void get() throws Exception {
		perform(MockMvcRequestBuilders
										.get(REST_URL + RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN)))
							.andExpect(status().isOk())
							.andDo(print())
							.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							.andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_1));
	}

	@Test
	void delete() throws Exception {
		perform(MockMvcRequestBuilders
										.delete(REST_URL + RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN)))
						.andExpect(status().isNoContent());

		assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT_1_ID));
	}

	@Test
	void enable() throws Exception {
		perform(MockMvcRequestBuilders
										.patch(REST_URL + RESTAURANT_1_ID)
										.param("enabled", "false")
										.contentType(MediaType.APPLICATION_JSON)
										.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isNoContent());

		assertFalse(restaurantService.get(RESTAURANT_1_ID).isEnabled());
	}

	@Test
	void create() throws Exception {
		Restaurant newRestaurant = getNewRestaurant();
		ResultActions action = perform(MockMvcRequestBuilders
																					 .post(REST_URL)
																					 .with(userHttpBasic(ADMIN))
																					 .contentType(MediaType.APPLICATION_JSON)
																					 .content(JsonUtil.writeValue(newRestaurant)))
						.andDo(print())
						.andExpect(status().isCreated());

		Restaurant created = readFromJson(action, Restaurant.class);

		int newId = created.getId();
		newRestaurant.setId(newId);

		RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
		RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
	}

	@Test
	void update() throws Exception {
		Restaurant updated = getUpdatedRestaurant();
		perform(MockMvcRequestBuilders
										.put(REST_URL + RESTAURANT_1_ID)
										.contentType(MediaType.APPLICATION_JSON)
										.with(userHttpBasic(ADMIN))
										.content(JsonUtil.writeValue(updated)))
						.andDo(print())
						.andExpect(status().isNoContent());

		RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT_1_ID), updated);
	}

	@Test
	void getDish() throws Exception {
		perform(MockMvcRequestBuilders
										.get(DISHES_REST_URL + DISH_1_ID, RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(DISH_MATCHER.contentJson(DISH_1));
	}

	@Test
	void getAllDishes() throws Exception {
		perform(MockMvcRequestBuilders
										.get(DISHES_REST_URL, RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void createDish() throws Exception {
		Dish newDish = getNewDish();
		ResultActions action = perform(MockMvcRequestBuilders.post(DISHES_REST_URL, RESTAURANT_1_ID)
																												 .with(userHttpBasic(ADMIN))
																												 .contentType(MediaType.APPLICATION_JSON)
																												 .content(JsonUtil.writeValue(newDish)))
						.andDo(print())
						.andExpect(status().isCreated());

		Dish created = readFromJson(action, Dish.class);
		int newId = created.id();
		newDish.setId(newId);

		DISH_MATCHER.assertMatch(created, newDish);
		DISH_MATCHER.assertMatch(dishService.get(newId, RESTAURANT_1_ID), newDish);
	}

	@Test
	void updateDish() throws Exception {
		Dish updated = getUpdatedDish();
		perform(MockMvcRequestBuilders
										.put(DISHES_REST_URL + DISH_1_ID, RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN))
										.contentType(MediaType.APPLICATION_JSON)
										.content(JsonUtil.writeValue(updated)))
						.andDo(print())
						.andExpect(status().isNoContent());

		DISH_MATCHER.assertMatch(dishService.get(DISH_1_ID, RESTAURANT_1_ID), updated);
	}

	@Test
	void deleteDish() throws Exception {
		perform(MockMvcRequestBuilders
										.delete(DISHES_REST_URL + DISH_1_ID, RESTAURANT_1_ID)
										.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isNoContent());

		assertThrows(NotFoundException.class, () -> dishService.get(DISH_1_ID, RESTAURANT_1_ID));
	}
}