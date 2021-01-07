package ru.iuriimudrak.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.iuriimudrak.restaurant.model.Role;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iuriimudrak.restaurant.UserTestData.*;

class UserServiceTest extends AbstractServiceTest {

	@Autowired
	protected UserService service;

	@Test
	void create() {
		User created = service.create(getNew());
		int newId = created.id();
		User newUser = getNew();
		newUser.setId(newId);
		USER_MATCHER.assertMatch(created, newUser);
		USER_MATCHER.assertMatch(service.get(newId), newUser);
	}

	@Test
	void duplicateMailCreate() {
		assertThrows(DataAccessException.class, () ->
						service.create(new User(null, "Duplicate", "user@gmail.com", "newPass", Role.USER)));
	}

	@Test
	void delete() {
		service.delete(USER_ID);
		assertThrows(NotFoundException.class, () -> service.get(USER_ID));
	}

	@Test
	void deleteNotFound() {
		assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
	}

	@Test
	void get() {
		User user = service.get(USER_ID);
		USER_MATCHER.assertMatch(user, USER);
	}

	@Test
	void getNotFound() {
		assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
	}

	@Test
	void getWithVotes() {
		User user = service.getWithVotes(USER_ID);
		USER_MATCHER.assertMatch(user, USER);
	}

	@Test
	void getByEmail() {
		User user = service.getByEmail("admin@gmail.com");
		USER_MATCHER.assertMatch(user, ADMIN);
	}

	@Test
	void update() {
		User updated = getUpdatedUser();
		service.update(updated);
		USER_MATCHER.assertMatch(service.get(USER_ID), getUpdatedUser());
	}

	@Test
	void getAll() {
		List<User> all = service.getAll();
		USER_MATCHER.assertMatch(all, ADMIN, USER);
	}

	@Test
	void createWithException() {
		validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)), ConstraintViolationException.class);
		validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.USER)), ConstraintViolationException.class);
		validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)), ConstraintViolationException.class);
	}
}