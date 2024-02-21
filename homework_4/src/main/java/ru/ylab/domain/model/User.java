package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User model
 */
@AllArgsConstructor
@Getter
public class User {
    private Long id;
    /**
     * Name of user
     */
    private String username;
    /**
     * Password of user
     */
    private String password;
}
