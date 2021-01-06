package ru.iuriimudrak.restaurant.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "localdate"}, name = "vote_unique_user_date_idx")})
public class Vote extends AbstractBaseEntity {

	public static final LocalTime VOTE_DDL = LocalTime.of(11, 0);

	@NotNull
	@Column(name = "localdate", nullable = false)
	private LocalDate localDate = LocalDate.now();

	@NotNull
	@JsonBackReference("userVotes")
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("restaurantVotes")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	public Vote(LocalDate localDate, User user, Restaurant restaurant) {
		this(null, localDate, user, restaurant);
	}

	public Vote(Integer id, LocalDate localDate, User user, Restaurant restaurant) {
		super(id);
		this.localDate = localDate;
		this.user = user;
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Vote{" +
						"id=" + id +
						", date=" + localDate +
						'}';
	}
}
