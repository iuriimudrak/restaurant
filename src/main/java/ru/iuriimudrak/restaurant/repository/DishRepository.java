package ru.iuriimudrak.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iuriimudrak.restaurant.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
	int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

	@Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.localDate=:localDate " +
					"ORDER BY d.name DESC")
	List<Dish> getByDate(@Param("localDate") LocalDate localDate, @Param("restaurantId") int restaurantId);

	@Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId " +
					"ORDER BY d.localDate DESC")
	List<Dish> getAll(@Param("restaurantId") int restaurantId);
}
