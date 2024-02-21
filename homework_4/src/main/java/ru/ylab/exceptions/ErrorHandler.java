package ru.ylab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	 * Handle wrong condition exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleWrongConditionException(final WrongDataException e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.BAD_REQUEST)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}

	/**
	 * Handle method argument not valid exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.BAD_REQUEST)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}

	/**
	 * Handle user already registered exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handleUserAlreadyRegisteredException(final UserAlreadyRegisteredException e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.CONFLICT)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}

	/**
	 * Handle no rights exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiError handleNoRightsException(final NoRightsException e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.FORBIDDEN)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}

	/**
	 * Handle null pointer exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleNullPointerException(final NullPointerException e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.NOT_FOUND)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}

	/**
	 * Handle exception api error.
	 *
	 * @param e the e
	 * @return the api error
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleException(final Exception e) {
		e.printStackTrace();
		return ApiError.builder()
				.message(e.getMessage())
				.name(e.getClass().getSimpleName())
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.timestamp(LocalDateTime.now().format(formatter))
				.build();
	}
}
