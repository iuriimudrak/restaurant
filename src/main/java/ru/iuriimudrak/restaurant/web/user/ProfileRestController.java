package ru.iuriimudrak.restaurant.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.to.UserTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.iuriimudrak.restaurant.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController extends AbstractUserController {

	public static final String REST_URL = "/profile";

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public User get() {
		return super.get(authUserId());
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
	public void delete() {
		super.delete(authUserId());
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody UserTo userTo) throws BindException {
		super.update(userTo, authUserId());
	}
}
