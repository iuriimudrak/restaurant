package ru.iuriimudrak.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name"},
																															 name = "dish_unique_restaurant_name_idx")})
public class Dish extends AbstractNamedEntity {

//	https://stackoverflow.com/a/8148773
	@Column(name = "price")
	@NotNull
	@Range(min = 1, max = 100000)
	private BigDecimal price;

	@Column(name = "localdate", nullable = false)
	@NotNull
	private LocalDate localDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull
	@JsonBackReference(value = "restaurantDishes")
	private Restaurant restaurant;

	public Dish(Integer id, LocalDate localDate, String name, BigDecimal price, Restaurant restaurant) {
		super(id, name);
		this.localDate = localDate;
		this.price = price;
		this.restaurant = restaurant;
	}

	public Dish(Dish dish) {
		this(dish.getId(), dish.getLocalDate(), dish.getName(), dish.getPrice(), dish.getRestaurant());
	}
}
