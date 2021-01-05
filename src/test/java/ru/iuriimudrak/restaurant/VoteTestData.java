package ru.iuriimudrak.restaurant;

import ru.iuriimudrak.restaurant.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static ru.iuriimudrak.restaurant.RestaurantTestData.*;
import static ru.iuriimudrak.restaurant.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
	public static TestMatcher<Vote> VOTE_FULL_MATCHER = TestMatcher.usingEqualsAssertions(Vote.class);
	public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "user", "restaurant");

	public static final int VOTE_1_ID = START_SEQ + 6;
	public static final int VOTE_2_ID = START_SEQ + 7;
	public static final int VOTE_3_ID = START_SEQ + 8;

	public static final Vote VOTE_1 = new Vote(VOTE_1_ID, LocalDate.of(2020, 12, 10), UserTestData.USER, RESTAURANT_2);
	public static final Vote VOTE_2 = new Vote(VOTE_2_ID, LocalDate.of(2020, 12, 11), UserTestData.USER, RESTAURANT_2);
	public static final Vote VOTE_3 = new Vote(VOTE_3_ID, LocalDate.of(2020, 12, 12), UserTestData.USER, RESTAURANT_4);

	public static final List<Vote> VOTES = List.of(VOTE_3, VOTE_2, VOTE_1);

	public static Vote getNewVote() {
		return new Vote(null, LocalDate.now(), UserTestData.USER, RESTAURANT_1);
	}
}