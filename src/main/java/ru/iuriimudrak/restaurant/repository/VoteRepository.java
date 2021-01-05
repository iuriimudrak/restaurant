package ru.iuriimudrak.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iuriimudrak.restaurant.model.User;
import ru.iuriimudrak.restaurant.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

	Vote getByUserAndLocalDate(User user, LocalDate localDate);

	Vote getByUserIdAndLocalDate(int userId, LocalDate localDate);

	@Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.localDate DESC")
	List<Vote> getAll(@Param("userId") int userId);
}
