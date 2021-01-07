package ru.iuriimudrak.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iuriimudrak.restaurant.AuthorizedUser;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.repository.UserRepository;
import ru.iuriimudrak.restaurant.to.UserTo;
import ru.iuriimudrak.restaurant.util.UserUtil;

import java.util.List;

import static ru.iuriimudrak.restaurant.util.RepositoryUtil.findById;
import static ru.iuriimudrak.restaurant.util.UserUtil.prepareToSave;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNotFound;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	@CacheEvict(value = "users", allEntries = true)
	public User create(User user) {
		Assert.notNull(user, "user must not be null");
		return prepareAndSave(user);
	}

	@CacheEvict(value = "users", allEntries = true)
	public void delete(int id) {
		checkNotFoundWithId(repository.delete(id) != 0, id);
	}

	public User get(int id) {
		return findById(repository, id);
	}

	public User getByEmail(String email) {
		Assert.notNull(email, "email must not be null");
		return checkNotFound(repository.getByEmail(email), "email=" + email);
	}

	public List<User> getAll() {
		return repository.findAll(
						Sort.by(Sort.Direction.ASC, "name", "email"));
	}

	@CacheEvict(value = "users", allEntries = true)
	public void update(User user) {
		Assert.notNull(user, "user must not be null");
		prepareAndSave(user);
	}

	@Transactional
	@CacheEvict(value = "users", allEntries = true)
	public void update(UserTo userTo) {
		User user = get(userTo.id());
		prepareAndSave(UserUtil.updateFromTo(user, userTo));
	}

	public User getWithVotes(int id) {
		return checkNotFoundWithId(repository.getWithVotes(id), id);
	}

	@Transactional
	@CacheEvict(value = "users", allEntries = true)
	public void enable(int id, boolean enabled) {
		User user = get(id);
		user.setEnabled(enabled);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository.getByEmail(email.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException("User " + email + " is not found");
		}
		return new AuthorizedUser(user);
	}

	private User prepareAndSave(User user) {
		return repository.save(prepareToSave(user, passwordEncoder));
	}
}
