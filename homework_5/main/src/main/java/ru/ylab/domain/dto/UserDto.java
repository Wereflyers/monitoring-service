package ru.ylab.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserDto {
    /**
     * Name of user
     */
    @NotNull(message = "Необходимо указать имя")
    @NotBlank(message = "Необходимо указать имя")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-\\s]{2,30}$", message = "Длина имени пользователя должна быть от 2 до 30 символов. " +
            "Цифры в имени не допускаются.")
    private String username;
    /**
     * Password of user
     */
    @NotNull(message = "Необходимо указать пароль")
    @NotBlank(message = "Необходимо указать пароль")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,30}$",
            message = "Пароль должен соответствовать следующим требованиям: " +
            "1. Использование строчной и прописной буквы;" +
            "2. Использование цифры от 0 до 9;" +
            "3. Длина пароля от 8 до 30 символов;")
    private String password;
}
