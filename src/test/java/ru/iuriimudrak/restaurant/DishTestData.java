package ru.iuriimudrak.restaurant;

import ru.iuriimudrak.restaurant.model.Dish;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.iuriimudrak.restaurant.RestaurantTestData.RESTAURANT_1;
import static ru.iuriimudrak.restaurant.model.AbstractBaseEntity.START_SEQ;


public class DishTestData {
	public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Dish.class, "restaurant");

	public static final LocalDate DISH_TEST_DATE = LocalDate.of(2020, 12, 10);
	public static final int DISH_1_ID = START_SEQ + 13;
	public static final int DISH_2_ID = START_SEQ + 14;
	public static final int DISH_3_ID = START_SEQ + 15;

	public static final Dish DISH_1 = new Dish(DISH_1_ID, DISH_TEST_DATE, "Risotto", new BigDecimal(500), RESTAURANT_1);
	public static final Dish DISH_2 = new Dish(DISH_2_ID, DISH_TEST_DATE, "Borsch", new BigDecimal(300), RESTAURANT_1);
	public static final Dish DISH_3 = new Dish(DISH_3_ID, DISH_TEST_DATE, "Cheesecake", new BigDecimal(350), RESTAURANT_1);

	public static Dish getNewDish() {
		return new Dish(null, LocalDate.now(), "New", new BigDecimal(1000), RESTAURANT_1);
	}

	public static Dish getUpdatedDish() {
		Dish updated = new Dish(DISH_1);
		updated.setName("UpdatedName");
		return updated;
	}

}