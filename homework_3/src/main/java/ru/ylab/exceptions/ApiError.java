package ru.ylab.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Api error.
 */
@Getter
public class ApiError {
    private final String message;
    private final String reason;
    private final String timestamp;

    /**
     * Instantiates a new Api error.
     *
     * @param message the message
     * @param reason  the reason
     */
    public ApiError(String message, String reason) {
        this.message = message;
        this.reason = reason;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm"));
    }
}
