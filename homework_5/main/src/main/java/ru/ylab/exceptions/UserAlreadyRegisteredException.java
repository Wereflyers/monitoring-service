package ru.ylab.exceptions;

/**
 * The type User already registered exception.
 */
public class UserAlreadyRegisteredException extends RuntimeException {
    /**
     * Instantiates a new User already registered exception.
     *
     * @param message the message
     */
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
