package ru.iuriimudrak.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;
import ru.iuriimudrak.restaurant.repository.RestaurantRepository;
import ru.iuriimudrak.restaurant.repository.UserRepository;
import ru.iuriimudrak.restaurant.repository.VoteRepository;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.iuriimudrak.restaurant.model.Vote.VOTE_DDL;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class VoteService {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final VoteRepository voteRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;

	@Setter
	private Clock clock = Clock.systemDefaultZone();

	@Transactional
	@CacheEvict(value = "restaurantTos", allEntries = true)
	public Vote setVote(int userId, int restaurantId) {

		LocalDateTime votingLocalDateTime = LocalDateTime.now(clock);
		final Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getOne(restaurantId), restaurantId);
		final User user = checkNotFoundWithId(userRepository.getOne(userId), userId);

		Vote vote;

		try {
			vote = getByUserAndLocalDate(user, votingLocalDateTime.toLocalDate());
		} catch (NotFoundException e) {
			log.debug("set vote from user id= {} to restaurant id= {}", userId, restaurantId);
			return voteRepository.save(new Vote(null, votingLocalDateTime.toLocalDate(), user, restaurant));
		}

		if (votingLocalDateTime.toLocalTime().isBefore(VOTE_DDL)) {
			if (vote.getRestaurant().id() != restaurantId) {
				vote.setRestaurant(restaurant);
				log.debug("vote have been changed: from user id= {} to restaurant id= {}", userId, restaurantId);
				return vote;
			}
			log.debug("vote have NOT been changed: from user id= {} to restaurant id= {}", userId, restaurantId);
			return vote;
		} else {
			throw new RuntimeException("Deadline for voting has been passed");
		}
	}

	public Vote getByUserAndLocalDate(User user, LocalDate date) {
		return checkNotFoundWithId(voteRepository.getByUserAndLocalDate(user, date), user.id());
	}

	public Vote getByUserIdAndLocalDate(int userId, LocalDate date) {
		return checkNotFoundWithId(voteRepository.getByUserIdAndLocalDate(userId, date), userId);
	}

	public List<Vote> getAll(int userId) {
		return voteRepository.getAll(userId);
	}
}
