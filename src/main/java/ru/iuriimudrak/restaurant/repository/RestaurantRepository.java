package ru.iuriimudrak.restaurant.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iuriimudrak.restaurant.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Restaurant r WHERE r.id=:id")
	int delete(@Param("id") int id);

// https://stackoverflow.com/a/38422635
	@EntityGraph(attributePaths = {"dishes"})
	@Query("SELECT r FROM Restaurant r JOIN r.dishes d " +
					"WHERE d.localDate=:date")
	List<Restaurant> getAllByDate(@Param("date") LocalDate date);
}
