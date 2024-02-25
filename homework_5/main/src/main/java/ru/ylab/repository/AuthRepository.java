package ru.ylab.repository;

import ru.ylab.domain.model.User;

import java.sql.SQLException;

/**
 * The interface Auth repository.
 */
public interface AuthRepository {
    /**
     * Register user.
     *
     * @param username the username
     * @param password the password
     * @return username
     * @throws SQLException the sql exception
     */
    String registerUser(String username, String password) throws SQLException;

    /**
     * Check user in db
     *
     * @param username the username
     * @return boolean
     * @throws SQLException the sql exception
     */
    boolean hasUser(String username) throws SQLException;

    /**
     * Auth user.
     *
     * @param username the username
     * @return username
     * @throws SQLException the sql exception
     */
    User getUser(String username) throws SQLException;
}
