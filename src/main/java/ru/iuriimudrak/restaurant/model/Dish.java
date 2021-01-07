package ru.iuriimudrak.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

	@NotNull
	@Positive
	@Column(name = "price")
	private BigDecimal price;

	@NotNull
	@Column(name = "localdate", nullable = false)
	private LocalDate localDate = LocalDate.now();

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonBackReference(value = "restaurantDishes")
	@JoinColumn(name = "restaurant_id", nullable = false)
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
