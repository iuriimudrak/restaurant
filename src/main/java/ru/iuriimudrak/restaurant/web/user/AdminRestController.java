package ru.iuriimudrak.restaurant.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController extends AbstractUserController {

	public static final String REST_URL = "/admin/users",
					VOTE_URL = "/votes";

	@Override
	@GetMapping
	public List<User> getAll() {
		return super.getAll();
	}

	@Override
	@GetMapping("/{id}")
	public User get(@PathVariable int id) {
		return super.get(id);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
	@ResponseStatus(value = HttpStatus.CREATED) // 201
	public ResponseEntity<User> createWithLocation(@RequestBody User user) {
		User created = super.create(user);
		URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
																											.path(REST_URL + "/{id}")
																											.buildAndExpand(created.getId())
																											.toUri();
		return ResponseEntity.created(uriOfNewResource)
												 .body(created);
	}

	@GetMapping("/by")
	public User getByMail(@RequestParam String email) {
		return super.getByMail(email);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	public void update(@RequestBody User user, @PathVariable int id) {
		super.update(user, id);
	}

	@Override
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void enable(@PathVariable int id, @RequestParam boolean enabled) {
		super.enable(id, enabled);
	}

	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		super.delete(id);
	}

	@GetMapping("/{id}" + VOTE_URL)
	public List<Vote> getAllVotes(@PathVariable int id) {
		return super.getAllVotes(id);
	}

	@GetMapping("/{id}/with-votes")
	public User getWithVotes(@PathVariable int id) {
		return super.getWithVotes(id);
	}

	@GetMapping("/{id}" + VOTE_URL + "/by")
	public Vote getByDate(@PathVariable int id, @RequestParam LocalDate date) {
		return super.getVoteByDate(id, date);
	}
}
