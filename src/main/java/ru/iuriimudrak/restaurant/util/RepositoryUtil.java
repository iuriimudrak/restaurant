package ru.iuriimudrak.restaurant.util;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.iuriimudrak.restaurant.util.exception.NotFoundException;

@NoArgsConstructor
public class RepositoryUtil {

	// Usage of findById over getOne
	// https://stackoverflow.com/a/47370947
	public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id " + id));
	}
}
