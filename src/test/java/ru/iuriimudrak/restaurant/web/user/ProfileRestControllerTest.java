package ru.iuriimudrak.restaurant.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.service.UserService;
import ru.iuriimudrak.restaurant.to.UserTo;
import ru.iuriimudrak.restaurant.util.UserUtil;
import ru.iuriimudrak.restaurant.web.AbstractControllerTest;
import ru.iuriimudrak.restaurant.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iuriimudrak.restaurant.TestUtil.readFromJson;
import static ru.iuriimudrak.restaurant.TestUtil.userHttpBasic;
import static ru.iuriimudrak.restaurant.UserTestData.*;
import static ru.iuriimudrak.restaurant.VoteTestData.VOTE_1;
import static ru.iuriimudrak.restaurant.VoteTestData.VOTE_MATCHER;
import static ru.iuriimudrak.restaurant.web.user.ProfileRestController.REST_URL;
import static ru.iuriimudrak.restaurant.web.user.ProfileRestController.VOTE_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

	@Autowired
	private UserService userService;


	@Test
	void get() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL)
																	.with(userHttpBasic(USER)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(USER_MATCHER.contentJson(USER));
	}

	@Test
	void getUnAuth() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL))
						.andExpect(status().isUnauthorized());
	}

	@Test
	void delete() throws Exception {
		perform(MockMvcRequestBuilders.delete(REST_URL)
																	.with(userHttpBasic(USER)))
						.andDo(print())
						.andExpect(status().isNoContent());
		USER_MATCHER.assertMatch(userService.getAll(), ADMIN);
	}

	@Test
	void register() throws Exception {
		UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
		User newUser = UserUtil.createNewFromTo(newTo);
		ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
																												 .contentType(MediaType.APPLICATION_JSON)
																												 .content(JsonUtil.writeValue(newTo)))
						.andDo(print())
						.andExpect(status().isCreated());

		User created = readFromJson(action, User.class);
		int newId = created.getId();
		newUser.setId(newId);
		USER_MATCHER.assertMatch(created, newUser);
		USER_MATCHER.assertMatch(userService.get(newId), newUser);
	}

	@Test
	void update() throws Exception {
		UserTo updatedTo = new UserTo(null, "newName", "user@gmail.com", "newPassword");
		perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
																	.with(userHttpBasic(USER))
																	.content(JsonUtil.writeValue(updatedTo)))
						.andDo(print())
						.andExpect(status().isNoContent());

		USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
	}

	@Test
	void getWithVotes() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + "/with-votes")
																	.with(userHttpBasic(USER)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(USER_MATCHER.contentJson(USER));
	}

	@Test
	void getAllVotes() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + VOTE_URL)
																	.with(userHttpBasic(USER)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void getVoteByDate() throws Exception {
		perform(MockMvcRequestBuilders.get(REST_URL + VOTE_URL + "/by?date=" + VOTE_1.getLocalDate())
																	.with(userHttpBasic(USER)))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(VOTE_MATCHER.contentJson(VOTE_1));
	}
}