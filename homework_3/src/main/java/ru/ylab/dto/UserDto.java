package ru.ylab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    /**
     * Name of user
     */
    private String username;
    /**
     * Password of user
     */
    private String password;
}
