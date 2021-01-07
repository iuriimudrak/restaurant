package ru.iuriimudrak.restaurant.web.restaurant;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iuriimudrak.restaurant.model.Dish;
import ru.iuriimudrak.restaurant.model.Restaurant;
import ru.iuriimudrak.restaurant.service.DishService;
import ru.iuriimudrak.restaurant.service.RestaurantService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.iuriimudrak.restaurant.util.ValidationUtil.assureIdConsistent;
import static ru.iuriimudrak.restaurant.util.ValidationUtil.checkNew;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AdminRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController {

	protected static final String REST_URL = "/admin/restaurants";
	protected static final String DISH_URL = "/{restaurantId}/dishes";
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final RestaurantService restaurantService;
	private final DishService dishService;

	@GetMapping("/{id}")
	public Restaurant get(@PathVariable int id) {
		log.info("get restaurant id= {}", id);

		return restaurantService.get(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		log.info("delete restaurant id= {}", id);
		restaurantService.delete(id);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void enable(@PathVariable int id, @RequestParam boolean enabled) {
		log.info(enabled ? "enable {}" : "disable {}", id);
		restaurantService.enable(id, enabled);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
		log.info("create restaurant id= {}", restaurant);
		Restaurant created = restaurantService.create(restaurant);
		URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
																											.path(REST_URL + "/{id}")
																											.buildAndExpand(created.getId())
																											.toUri();

		return ResponseEntity.created(uriOfNewResource)
												 .body(created);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
		log.info("update restaurant id= {}", restaurant);
		assureIdConsistent(restaurant, id);
		restaurantService.update(restaurant);
	}

	@GetMapping(value = DISH_URL + "/{dishId}")
	public Dish getDish(@PathVariable int restaurantId, @PathVariable int dishId) {
		log.info("get dish id= {}", dishId);
		return dishService.get(dishId, restaurantId);
	}

	@GetMapping(value = DISH_URL)
	public List<Dish> getAllDishes(@PathVariable int restaurantId) {
		log.info("getAllDishes for restaurant id= {}", restaurantId);
		return dishService.getAll(restaurantId);
	}

	@PostMapping(value = DISH_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish> createDish(@PathVariable int restaurantId, @RequestBody Dish dish) {
		log.info("restaurant {} adding dish {}", restaurantId, dish);
		checkNew(dish);
		Dish created = dishService.create(dish, restaurantId);
		URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
																											.path(REST_URL + DISH_URL + "/{dishId}")
																											.buildAndExpand(restaurantId, created.getId()).toUri();
		return ResponseEntity.created(uriOfNewResource).body(created);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping(value = DISH_URL + "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateDish(@RequestBody Dish dish, @PathVariable int dishId, @PathVariable int restaurantId) {
		log.info("update dish {} for restaurant id= {}", dish, restaurantId);
		assureIdConsistent(dish, dishId);
		dishService.update(dish, restaurantId);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = DISH_URL + "/{dishId}")
	public void deleteDish(@PathVariable int restaurantId, @PathVariable int dishId) {
		log.info("delete dish {} for restaurant id= {}", dishId, restaurantId);
		dishService.delete(dishId, restaurantId);
	}
}
