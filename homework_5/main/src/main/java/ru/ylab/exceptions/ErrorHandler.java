package ru.ylab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Exception handler.
 */
@RestControllerAdvice
public class ErrorHandler {
	/**
	 * Date Time Formatter
	 */
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Handle exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	public ResponseEntity<ApiError> handleException(final Exception e) {
		e.printStackTrace();
		HttpStatus status = ExceptionMapper.getHttpStatusByExceptionName(e.getClass().getSimpleName());
		return ResponseEntity.status(status)
				.body(ApiError.builder()
						.message(e.getMessage())
						.name(e.getClass().getSimpleName())
						.status(status)
						.timestamp(LocalDateTime.now().format(formatter))
						.build());
	}
}
