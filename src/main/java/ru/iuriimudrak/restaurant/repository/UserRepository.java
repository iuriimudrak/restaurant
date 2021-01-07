package ru.iuriimudrak.restaurant.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iuriimudrak.restaurant.model.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM User u WHERE u.id=:id")
	int delete(@Param("id") int id);

	@Cacheable("users")
	User getByEmail(String email);

	@EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT u FROM User u WHERE u.id=:id")
	User getWithVotes(@Param("id") int id);
}
