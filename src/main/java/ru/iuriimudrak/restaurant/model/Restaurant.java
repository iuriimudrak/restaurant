package ru.iuriimudrak.restaurant.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {

	@Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
	private boolean enabled = true;

	@NotNull
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
	private Date registered = new Date();

	@OrderBy("localDate DESC")
	@JsonManagedReference("restaurantVotes")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
	private List<Vote> votes;

	@OrderBy("localDate DESC")
	@JsonManagedReference(value = "restaurantDishes")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
	private List<Dish> dishes;

	public Restaurant(Integer id, String name, boolean enabled, Date registered) {
		super(id, name);
		this.enabled = enabled;
		this.registered = registered;
	}

	public Restaurant(Integer id, String name) {
		this(id, name, true, new Date());
	}

	public Restaurant(Restaurant restaurant) {
		this(restaurant.getId(), restaurant.getName(), restaurant.isEnabled(), restaurant.getRegistered());
	}

	@Override
	public String toString() {
		return "Restaurant{" +
						"id=" + id +
						", name='" + name + "'" +
						", enabled=" + enabled +
						'}';
	}
}
