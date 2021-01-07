package ru.iuriimudrak.restaurant.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iuriimudrak.restaurant.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iuriimudrak.restaurant.DishTestData.DISH_1;
import static ru.iuriimudrak.restaurant.DishTestData.DISH_TEST_DATE;
import static ru.iuriimudrak.restaurant.RestaurantTestData.RESTAURANT_1;
import static ru.iuriimudrak.restaurant.TestUtil.userHttpBasic;
import static ru.iuriimudrak.restaurant.UserTestData.USER;

class UserRestaurantRestControllerTest extends AbstractControllerTest {

	private static final String REST_URL = UserRestaurantRestController.REST_URL + '/';
	private static final String DISHES_REST_URL = UserRestaurantRestController.REST_URL+ UserRestaurantRestController.DISH_URL + '/';

	@Test
	void getAllByDate() throws Exception {
		perform(MockMvcRequestBuilders
										.get(REST_URL + "by?date=" + DISH_TEST_DATE)
										.with(userHttpBasic(USER)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void getDish() throws Exception {
		perform(MockMvcRequestBuilders
										.get(DISHES_REST_URL + DISH_1.id(), RESTAURANT_1.id())
										.with(userHttpBasic(USER)))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void getDishesByDate() throws Exception {
		perform(MockMvcRequestBuilders
										.get(DISHES_REST_URL + "by?date=" + DISH_1.getLocalDate(), RESTAURANT_1.id())
										.with(userHttpBasic(USER)))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}