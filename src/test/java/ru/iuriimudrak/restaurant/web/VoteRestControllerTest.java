package ru.iuriimudrak.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iuriimudrak.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static ru.iuriimudrak.restaurant.TestUtil.userHttpBasic;
import static ru.iuriimudrak.restaurant.UserTestData.USER;
import static ru.iuriimudrak.restaurant.web.VoteRestController.REST_URL;

class VoteRestControllerTest extends AbstractControllerTest {

	@Test
	void setVote() throws Exception {
		perform(MockMvcRequestBuilders.post(REST_URL)
																	.param("restaurantId", String.valueOf(RESTAURANT_1_ID))
																	.with(userHttpBasic(USER))
																	.contentType(MediaType.APPLICATION_JSON)
																	.content(""))
																	.andExpect(status().isCreated());
	}
}