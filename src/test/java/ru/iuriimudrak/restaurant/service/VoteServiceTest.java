package ru.iuriimudrak.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iuriimudrak.restaurant.model.Vote;

import java.time.Clock;
import java.util.List;

import static ru.iuriimudrak.restaurant.UserTestData.USER_ID;
import static ru.iuriimudrak.restaurant.VoteTestData.*;
import static ru.iuriimudrak.restaurant.model.Vote.VOTE_DDL;
import static ru.iuriimudrak.restaurant.util.DateTimeUtil.createClock;

class VoteServiceTest extends AbstractServiceTest {

	@Autowired
	protected VoteService voteService;

	@Test
	void setVote() {
		Vote newVote = getNewVote();
		Vote created = voteService.setVote(newVote.getUser().id(), newVote.getRestaurant().id());
		newVote.setId(created.getId());
		VOTE_FULL_MATCHER.assertMatch(created, newVote);
		VOTE_FULL_MATCHER.assertMatch(voteService.getByUserAndLocalDate(created.getUser(), created.getLocalDate()), newVote);
	}

	@Test
	void getAll() {
		List<Vote> voteList = voteService.getAll(USER_ID);
		VOTE_MATCHER.assertMatch(voteList, VOTE_3, VOTE_2, VOTE_1);
	}

	@Test
	void getByUserAndLocalDate() {
		Vote result = voteService.getByUserIdAndLocalDate(USER_ID, VOTE_1.getLocalDate());
		VOTE_MATCHER.assertMatch(result, VOTE_1);
	}

	@Test
	void repeatVoteBeforeDDL() {
		Clock clock = createClock(VOTE_1.getLocalDate(), VOTE_DDL.minusMinutes(60));
		voteService.setClock(clock);

		Vote newVote = getNewVote();
		Vote created = voteService.setVote(newVote.getUser().id(),
																			 newVote.getRestaurant().id());

		newVote.setId(created.getId());

		VOTE_FULL_MATCHER.assertMatch(created, newVote);
		VOTE_FULL_MATCHER.assertMatch(voteService.getByUserAndLocalDate(created.getUser(), created.getLocalDate()), newVote);
	}

	@Test
	void repeatVoteAfterDDL() {
		Clock clock = createClock(VOTE_1.getLocalDate(), VOTE_DDL);
		voteService.setClock(clock);

		Vote newVote = getNewVote();

		validateRootCause(
						() -> voteService.setVote(newVote.getUser().id(),
																	    newVote.getRestaurant().id()),
																	    RuntimeException.class);
	}
}