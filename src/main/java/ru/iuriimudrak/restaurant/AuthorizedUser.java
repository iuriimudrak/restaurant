package ru.iuriimudrak.restaurant;

import lombok.Getter;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.to.UserTo;
import ru.iuriimudrak.restaurant.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	@Getter
	private UserTo userTo;

	public AuthorizedUser(User user) {
		super(user.getEmail(), user.getPassword(), user.isEnabled(),
					true, true, true, user.getRoles());
		this.userTo = UserUtil.asTo(user);
	}

	public int getId() {
		return userTo.id();
	}

	public void update(UserTo newTo) {
		userTo = newTo;
	}

	@Override
	public String toString() {
		return userTo.toString();
	}
}
