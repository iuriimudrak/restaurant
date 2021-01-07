package ru.iuriimudrak.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.iuriimudrak.restaurant.util.RepositoryUtil.findById;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	public Restaurant get(int id) {
		return findById(restaurantRepository, id);
	}

	@CacheEvict(value = "restaurants", allEntries = true)
	public Restaurant create(Restaurant restaurant) {
		Assert.notNull(restaurant, "Restaurant must not be null");
		return restaurantRepository.save(restaurant);
	}

	@CacheEvict(value = "restaurants", allEntries = true)
	public void delete(int id) {
		checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
	}

	@Cacheable("restaurants")
	public List<Restaurant> getAllByDate(LocalDate date) {
		return restaurantRepository.getAllByDate(date);
	}

	@CacheEvict(value = "restaurants", allEntries = true)
	public void update(Restaurant restaurant) {
		Assert.notNull(restaurant, "Restaurant must not be null");
		checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
	}

	@CacheEvict(value = "restaurants", allEntries = true)
	@Transactional
	public void enable(int id, boolean enabled) {
		Restaurant restaurant = get(id);
		restaurant.setEnabled(enabled);
	}
}
