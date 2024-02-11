package ru.ylab;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab.service.AuthService;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;

class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthService();
    }

    @Test
    @DisplayName(value = "Тест успешной регистрации пользователя")
    @SneakyThrows
    void registerUserTest() {
        authService.registerUser("name", "pass");

        Field privateField
                = AuthService.class.getDeclaredField("authDetails");
        privateField.setAccessible(true);
        HashMap<String, String> map = (HashMap<String, String>) privateField.get(authService);

        assertThat(map.get("name"))
                .isEqualTo("pass");
    }

    @Test
    @DisplayName(value = "Тест регистрации уже зарегистрированного пользователя")
    void registerUserTest_whenAlreadyRegistered() {
        authService.registerUser("name", "pass");

        assertThat(authService.registerUser("name", "p"))
                .isNull();
    }

    @Test
    @DisplayName(value = "Тест авторизации пользователя с неверным паролем")
    void authUserTest_whenPasswordsNotMatch() {
        authService.registerUser("name", "pass");

        assertThat(authService.authUser("name", "p"))
                .isNull();
    }

    @Test
    @DisplayName(value = "Тест авторизации незарегистрированного пользователя")
    void authUserTest_whenNotRegistered() {
        assertThat(authService.authUser("name", "p"))
                .isNull();
    }
}