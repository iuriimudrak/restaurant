package ru.iuriimudrak.restaurant.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;
import ru.iuriimudrak.restaurant.service.UserService;
import ru.iuriimudrak.restaurant.service.VoteService;
import ru.iuriimudrak.restaurant.to.UserTo;
import ru.iuriimudrak.restaurant.util.UserUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.iuriimudrak.restaurant.util.ValidationUtil.assureIdConsistent;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected UserService userService;

	@Autowired
	protected VoteService voteService;

	public List<User> getAll() {
		log.info("getAll");
		return userService.getAll();
	}

	public User get(int id) {
		log.info("get {}", id);
		return userService.get(id);
	}

	public User create(User user) {
		log.info("create {}", user);
		checkNew(user);
		return userService.create(user);
	}

	public User create(UserTo userTo) {
		log.info("create from to {}", userTo);
		return create(UserUtil.createNewFromTo(userTo));
	}

	public void delete(int id) {
		log.info("delete {}", id);
		userService.delete(id);
	}

	public void update(User user, int id) {
		log.info("update {} with id={}", user, id);
		assureIdConsistent(user, id);
		userService.update(user);
	}

	public void update(UserTo userTo, int id) {
		log.info("update {} with id={}", userTo, id);
		assureIdConsistent(userTo, id);
		userService.update(userTo);
	}

	public User getByMail(String email) {
		log.info("getByEmail {}", email);
		return userService.getByEmail(email);
	}

	public void enable(int id, boolean enabled) {
		log.info(enabled ? "enable {}" : "disable {}", id);
		userService.enable(id, enabled);
	}

	public User getWithVotes(int id) {
		log.info("getWithVotes {}", id);
		return userService.getWithVotes(id);
	}

	public Vote getTodayVote(int id) {
		return getVoteByDate(id, LocalDate.now());
	}

	public Vote getVoteByDate(int id, LocalDate date) {
		log.info("getVoteByDate for {} by {}", id, date);
		return voteService.getByUserIdAndLocalDate(id, date);
	}

	public List<Vote> getAllVotes(int id) {
		log.info("getAllVotes {}", id);
		return voteService.getAll(id);
	}
}
