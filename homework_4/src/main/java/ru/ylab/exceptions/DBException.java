package ru.ylab.exceptions;

/**
 * The type Some sql exception.
 */
public class DBException extends RuntimeException {
    /**
     * Instantiates a new Some sql exception.
     *
     * @param message the message
     */
    public DBException(String message) {
        super(message);
    }
}
