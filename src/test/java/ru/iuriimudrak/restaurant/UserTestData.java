package ru.iuriimudrak.restaurant;

import ru.iuriimudrak.restaurant.model.Role;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static ru.iuriimudrak.restaurant.model.AbstractBaseEntity.START_SEQ;
import static ru.iuriimudrak.restaurant.VoteTestData.VOTES;

public class UserTestData {
	public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(User.class, "registered", "votes", "password");

	public static final int NOT_FOUND = 10;
	public static final int USER_ID = START_SEQ;
	public static final int ADMIN_ID = START_SEQ + 1;

	public static final User USER = new User(USER_ID, "User", "user@gmail.com", "user", Role.USER);
	public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

	static {
		USER.setVotes(VOTES);
	}

	public static User getNew() {
		return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
	}

	public static User getUpdatedUser() {
		User updated = new User(USER);
		updated.setName("updName");
		return updated;
	}

	public static String jsonWithPassword(User user, String password) {
		return JsonUtil.writeAdditionProps(user, "password", password);
	}

}