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
    private final int code;

    /**
     * Instantiates a new Api error.
     *
     * @param message the message
     * @param reason  the reason
     * @param code the response code of exception
     */
    public ApiError(String message, String reason, int code) {
        this.message = message;
        this.reason = reason;
        this.code = code;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm"));
    }
}
