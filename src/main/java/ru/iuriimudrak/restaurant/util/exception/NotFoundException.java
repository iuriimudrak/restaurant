package ru.iuriimudrak.restaurant.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
// https://www.baeldung.com/spring-response-status
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Found no data")
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
