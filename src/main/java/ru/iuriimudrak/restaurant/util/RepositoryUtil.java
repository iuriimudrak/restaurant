package ru.iuriimudrak.restaurant.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryUtil {
	public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id " + id));
	}
}
