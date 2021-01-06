package ru.iuriimudrak.restaurant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iuriimudrak.restaurant.AuthorizedUser;
import ru.iuriimudrak.restaurant.model.Vote;
import ru.iuriimudrak.restaurant.service.VoteService;

import java.net.URI;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	protected static final String REST_URL = "/votes";

	private final VoteService voteService;

	@Autowired
	public VoteController(VoteService voteService) {
		this.voteService = voteService;
	}

	@PostMapping
	public ResponseEntity<Vote> setVote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
		log.info("Set vote for restaurant id= {}", restaurantId);
		Vote created = voteService.setVote(authorizedUser.getId(), restaurantId);
		URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
																											.path(REST_URL + "/{id}")
																											.buildAndExpand(created.getId()).toUri();
		return ResponseEntity.created(uriOfNewResource).body(created);
	}
}
