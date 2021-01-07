package ru.iuriimudrak.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iuriimudrak.restaurant.model.Dish;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iuriimudrak.restaurant.DishTestData.*;
import static ru.iuriimudrak.restaurant.RestaurantTestData.RESTAURANT_1_ID;

class DishServiceTest extends AbstractServiceTest {

	@Autowired
	protected DishService dishService;

	@Test
	void delete() {
		dishService.delete(DISH_1_ID, RESTAURANT_1_ID);
		assertThrows(NotFoundException.class, () -> dishService.get(DISH_1_ID, RESTAURANT_1_ID));
	}

	@Test
	void get() {
		Dish actual = dishService.get(DISH_1_ID, RESTAURANT_1_ID);
		DISH_MATCHER.assertMatch(actual, DISH_1);
	}

	@Test
	void getByDate() {
		DISH_MATCHER.assertMatch(dishService.getByDate(DISH_TEST_DATE, RESTAURANT_1_ID), DISH_1, DISH_3, DISH_2);
	}

	@Test
	void create() {
		Dish created = dishService.create(getNewDish(), RESTAURANT_1_ID);
		int newId = created.id();
		Dish newDish = getNewDish();
		newDish.setId(newId);

		DISH_MATCHER.assertMatch(created, newDish);
		DISH_MATCHER.assertMatch(dishService.get(newId, RESTAURANT_1_ID), newDish);
	}

	@Test
	void update() {
		Dish updated = getUpdatedDish();
		dishService.update(updated, RESTAURANT_1_ID);
		DISH_MATCHER.assertMatch(dishService.get(DISH_1_ID, RESTAURANT_1_ID), getUpdatedDish());
	}
}