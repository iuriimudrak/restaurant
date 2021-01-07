package ru.iuriimudrak.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iuriimudrak.restaurant.DishTestData.DISH_TEST_DATE;
import static ru.iuriimudrak.restaurant.RestaurantTestData.*;

class RestaurantServiceTest extends AbstractServiceTest {

	@Autowired
	protected RestaurantService restaurantService;

	@Test
	void get() {
		Restaurant restaurant = restaurantService.get(RESTAURANT_3_ID);
		RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT_3);
	}

	@Test
	void create() {
		Restaurant created = restaurantService.create(getNewRestaurant());
		int newId = created.id();
		Restaurant newRestaurant = getNewRestaurant();
		newRestaurant.setId(newId);
		RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
		RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
	}

	@Test
	void delete() {
		restaurantService.delete(RESTAURANT_1_ID);
		assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT_1_ID));
	}

	@Test
	void getAllByDate() {
		List<Restaurant> allByDate = restaurantService.getAllByDate(DISH_TEST_DATE);
		allByDate.forEach(System.out::println);
	}

	@Test
	void update() {
		Restaurant updated = getUpdatedRestaurant();
		restaurantService.update(updated);
		RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT_1_ID), getUpdatedRestaurant());
	}
}