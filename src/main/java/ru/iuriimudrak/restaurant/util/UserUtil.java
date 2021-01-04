package ru.iuriimudrak.restaurant.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.iuriimudrak.restaurant.model.Role;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.to.UserTo;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

public class UserUtil {

	public static User createNewFromTo(UserTo userTo) {
		return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
	}

	public static UserTo asTo(User user) {
		return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
	}

	public static User updateFromTo(User user, UserTo userTo) {
		user.setName(userTo.getName());
		user.setEmail(userTo.getEmail().toLowerCase());
		user.setPassword(userTo.getPassword());
		return user;
	}

	public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
		String password = user.getPassword();
		user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
		user.setEmail(user.getEmail().toLowerCase());
		return user;
	}

	// https://stackoverflow.com/a/47370947
	public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id " + id));
	}
}
