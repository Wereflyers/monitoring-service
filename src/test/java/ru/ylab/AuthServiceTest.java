package ru.ylab;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.exception.AccessDenialException;
import ru.ylab.service.AuthService;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthService();
    }

    @Test
    @SneakyThrows
    void registerUserTest() {
        authService.registerUser("name", "pass");

        Field privateField
                = AuthService.class.getDeclaredField("authDetails");
        privateField.setAccessible(true);
        HashMap<String, String> map = (HashMap<String, String>) privateField.get(authService);

        assertEquals("pass", map.get("name"));
    }

    @Test
    void registerUserTest_whenAlreadyRegistered() {
        authService.registerUser("name", "pass");

        assertThrows(AccessDenialException.class, () -> authService.registerUser("name", "p"));
    }

    @Test
    void authUserTest_whenPasswordsNotMatch() {
        authService.registerUser("name", "pass");

        assertThrows(AccessDenialException.class, () -> authService.authUser("name", "p"));
    }

    @Test
    void authUserTest_whenNotRegistered() {
        assertThrows(AccessDenialException.class, () -> authService.authUser("name", "p"));
    }
}