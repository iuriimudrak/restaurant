package ru.iuriimudrak.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iuriimudrak.restaurant.model.Dish;
import ru.iuriimudrak.restaurant.repository.DishRepository;
import ru.iuriimudrak.restaurant.repository.RestaurantRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.iuriimudrak.restaurant.util.RepositoryUtil.findById;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNew;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class DishService {

	private final DishRepository dishRepository;
	private final RestaurantRepository restaurantRepository;

	@CacheEvict(value = "restaurants", allEntries = true)
	public void delete(int id, int restaurantId) {
		checkNotFoundWithId(dishRepository.delete(id, restaurantId) != 0, id);
	}

	public Dish get(int id, int restaurantId) {
		return checkNotFoundWithId(dishRepository.findById(id)
																						 .filter(dish -> dish.getRestaurant().getId() == restaurantId)
																						 .orElse(null), id);
	}

	public List<Dish> getAll(int restaurantId) {
		return dishRepository.getAll(restaurantId);
	}

	public List<Dish> getByDate(LocalDate date, int restaurantId) {
		return dishRepository.getByDate(date, restaurantId);
	}

	@Transactional
	@CacheEvict(value = "restaurants", allEntries = true)
	public Dish create(@Valid Dish dish, int restaurantId) {
		Assert.notNull(dish, "Dish must not be null");
		checkNew(dish);
		dish.setRestaurant(findById(restaurantRepository, restaurantId));
		return dishRepository.save(dish);
	}

	@Transactional
	@CacheEvict(value = "restaurants", allEntries = true)
	public Dish update(@Valid Dish dish, int restaurantId) {
		Assert.notNull(dish, "Dish must not be null");
		checkNotFoundWithId(get(dish.id(), restaurantId), dish.id());
		dish.setRestaurant(findById(restaurantRepository, restaurantId));
		return checkNotFoundWithId(dishRepository.save(dish), dish.id());
	}
}
