package ru.iuriimudrak.restaurant;

import ru.iuriimudrak.restaurant.model.Restaurant;

import static ru.iuriimudrak.restaurant.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
	public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Restaurant.class, "registered", "dishes", "votes");

	public static final int RESTAURANT_1_ID = START_SEQ + 2;
	public static final int RESTAURANT_2_ID = START_SEQ + 3;
	public static final int RESTAURANT_3_ID = START_SEQ + 4;
	public static final int RESTAURANT_4_ID = START_SEQ + 5;

	public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Hide");
	public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Palkin");
	public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Moskovskiy");
	public static final Restaurant RESTAURANT_4 = new Restaurant(RESTAURANT_4_ID, "KFC");

	public static Restaurant getNewRestaurant() {
		return new Restaurant(null, "New");
	}

	public static Restaurant getUpdatedRestaurant() {
		Restaurant updated = new Restaurant(RESTAURANT_1);
		updated.setName("newName");
		return updated;
	}

}