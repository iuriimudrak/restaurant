package ru.iuriimudrak.restaurant.web.restaurant;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.iuriimudrak.restaurant.model.Dish;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.service.DishService;
import ru.iuriimudrak.restaurant.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController {

	protected static final String REST_URL = "/restaurants";
	protected static final String DISH_URL = "/{restaurantId}/dishes";
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final RestaurantService restaurantService;
	private final DishService dishService;

	@GetMapping
	public List<Restaurant> getAll() {
		log.info("get restaurants list by today");
		return restaurantService.getAllByDate(LocalDate.now());
	}

	@GetMapping("/by")
	public List<Restaurant> getAllByDate(@RequestParam LocalDate date) {
		log.info("get restaurants by date: {}", date);
		return restaurantService.getAllByDate(date);
	}

	@GetMapping(value = DISH_URL + "/{dishId}")
	@ResponseBody
	public Dish getDish(@PathVariable int restaurantId, @PathVariable int dishId) {
		log.info("get dish {} for restaurant id= {}", dishId, restaurantId);
		return dishService.get(dishId, restaurantId);
	}

	@GetMapping(value = DISH_URL + "/by")
	public List<Dish> getDishesByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
		log.info("get dishes for restaurant id= {} on date: {}", restaurantId, date);
		return dishService.getByDate(date, restaurantId);
	}
}
