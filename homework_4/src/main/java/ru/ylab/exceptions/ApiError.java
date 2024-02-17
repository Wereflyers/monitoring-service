package ru.ylab.exceptions;

import lombok.Builder;
import lombok.Getter;

/**
 * The type Api error.
 */
@Getter
@Builder
public class ApiError {
    private final String name;
    private final String message;
    private final String timestamp;
    private final String status;
}
