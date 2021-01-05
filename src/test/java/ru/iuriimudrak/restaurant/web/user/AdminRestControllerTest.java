package ru.iuriimudrak.restaurant.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iuriimudrak.restaurant.UserTestData;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.service.UserService;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;
import ru.iuriimudrak.restaurant.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iuriimudrak.restaurant.TestUtil.readFromJson;
import static ru.iuriimudrak.restaurant.TestUtil.userHttpBasic;
import static ru.iuriimudrak.restaurant.UserTestData.*;

class AdminRestControllerTest extends AbstractControllerTest {
	private static final String REST_URL = AdminRestController.REST_URL + '/';

	@Autowired
	private UserService userService;

	@Test
	void get() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
																	.with(userHttpBasic(ADMIN)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(USER_MATCHER.contentJson(ADMIN));
	}

	@Test
	void getNotFound() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + 1)
																	.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isUnprocessableEntity());
	}

	@Test
	void getByEmail() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + ADMIN.getEmail())
																	.with(userHttpBasic(ADMIN)))
						.andExpect(status().isOk())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(USER_MATCHER.contentJson(ADMIN));
	}

	@Test
	void delete() throws Exception {
		perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
																	.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isNoContent());
		assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
	}

	@Test
	void deleteNotFound() throws Exception {
		perform(MockMvcRequestBuilders.delete(REST_URL + 1)
																	.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isUnprocessableEntity());
	}

	@Test
	void getUnAuth() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL))
						.andExpect(status().isUnauthorized());
	}

	@Test
	void getForbidden() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL)
																	.with(userHttpBasic(USER)))
						.andExpect(status().isForbidden());
	}

	@Test
	void update() throws Exception {
		User updated = getUpdatedUser();
		updated.setId(null);
		perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
																	.contentType(MediaType.APPLICATION_JSON)
																	.with(userHttpBasic(ADMIN))
																	.content(UserTestData.jsonWithPassword(updated, "newPass")))
						.andExpect(status().isNoContent());

		USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdatedUser());
	}

	@Test
	void createWithLocation() throws Exception {
		User newUser = getNew();
		ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
																												 .contentType(MediaType.APPLICATION_JSON)
																												 .with(userHttpBasic(ADMIN))
																												 .content(UserTestData.jsonWithPassword(newUser, "newPass")))
						.andExpect(status().isCreated());

		User created = readFromJson(action, User.class);
		int newId = created.id();
		newUser.setId(newId);
		USER_MATCHER.assertMatch(created, newUser);
		USER_MATCHER.assertMatch(userService.get(newId), newUser);
	}

	@Test
	void getAll() throws Exception {
		perform(MockMvcRequestBuilders
										.get(REST_URL)
										.with(userHttpBasic(ADMIN)))
										.andDo(print())
										.andExpect(status().isOk())
										.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
										.andExpect(USER_MATCHER.contentJson(ADMIN, USER));
	}

	@Test
	void enable() throws Exception {
		perform(MockMvcRequestBuilders.patch(REST_URL + USER_ID)
																	.param("enabled", "false")
																	.contentType(MediaType.APPLICATION_JSON)
																	.with(userHttpBasic(ADMIN)))
						.andDo(print())
						.andExpect(status().isNoContent());

		assertFalse(userService.get(USER_ID)
													 .isEnabled());
	}
}
