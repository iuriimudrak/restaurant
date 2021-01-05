package ru.iuriimudrak.restaurant.json;

import org.junit.jupiter.api.Test;
import ru.iuriimudrak.restaurant.UserTestData;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;
import ru.iuriimudrak.restaurant.web.json.JsonUtil;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.iuriimudrak.restaurant.VoteTestData.*;

public class JsonUtilTest {
	@Test
	void readWriteValue() {
		String voteJson = JsonUtil.writeValue(VOTE_1);
		System.out.println(voteJson);
		Vote vote = JsonUtil.readValue(voteJson, Vote.class);
		VOTE_MATCHER.assertMatch(vote, VOTE_1);
	}

	@Test
	void readWriteValues() {
		String json = JsonUtil.writeValue(VOTES);
		System.out.println(json);
		List<Vote> votes = JsonUtil.readValues(json, Vote.class);
		VOTE_MATCHER.assertMatch(votes, VOTES);
	}

	@Test
	void writeOnlyAccess() {
		String json = JsonUtil.writeValue(UserTestData.USER);
		System.out.println(json);
		assertThat(json, not(containsString("password")));
		String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.USER, "newPass");
		System.out.println(jsonWithPass);
		User user = JsonUtil.readValue(jsonWithPass, User.class);
		assertEquals(user.getPassword(), "newPass");
	}
}
