package ru.iuriimudrak.restaurant.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iuriimudrak.restaurant.AuthorizedUser;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;
import ru.iuriimudrak.restaurant.to.UserTo;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController extends AbstractUserController {

	public static final String REST_URL = "/profile";
	public static final String VOTE_URL = "/votes";

	@GetMapping
	// https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/mvc.html
	public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
		return super.get(authUser.getId());
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED) // 201
	public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
		User created = super.create(userTo);
		URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
																											.path(REST_URL).build().toUri();
		return ResponseEntity.created(uriOfNewResource).body(created);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
		super.delete(authUser.getId());
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser) {
		super.update(userTo, authUser.getId());
	}

	@GetMapping(VOTE_URL + "/today")
	public Vote getTodayVote(@AuthenticationPrincipal AuthorizedUser authUser) {
		return super.getTodayVote(authUser.getId());
	}

	@GetMapping(VOTE_URL)
	public List<Vote> getAllVotes(@AuthenticationPrincipal AuthorizedUser authUser) {
		return super.getAllVotes(authUser.getId());
	}

	@GetMapping(VOTE_URL + "/by")
	public Vote getByDate(@AuthenticationPrincipal AuthorizedUser authUser, @RequestParam LocalDate date) {
		log.info("get vote for {} by date {}", authUser.getId(), date);
		return super.getVoteByDate(authUser.getId(), date);
	}

	@GetMapping("/with-votes")
	public User getWithVotes(@AuthenticationPrincipal AuthorizedUser authUser) {
		return super.getWithVotes(authUser.getId());
	}
}
