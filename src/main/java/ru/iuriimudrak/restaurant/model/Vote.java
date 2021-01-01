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
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "localdate"}, name = "vote_unique_user_date_idx")})
public class Vote extends AbstractBaseEntity {

	public static final LocalTime VOTE_DDL = LocalTime.of(11, 0);

	@Column(name = "localdate", nullable = false)
	@NotNull
	private LocalDate localDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull
	@JsonBackReference("userVotes")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull
	@JsonBackReference("restaurantVotes")
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
}
