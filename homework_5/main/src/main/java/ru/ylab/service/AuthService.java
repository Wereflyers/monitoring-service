package ru.ylab.service;

import ru.ylab.domain.model.User;

/**
 * AuthService is responsible for registration and authorization
 */
public interface AuthService {

    /**
     * Register user.
     *
     * @param user the user
     * @return username string
     */
    String registerUser(User user);

    /**
     * Auth user.
     *
     * @param user the user
     */
    void authUser(User user);

    /**
     * Is user exist boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean hasUser(String name);
}
