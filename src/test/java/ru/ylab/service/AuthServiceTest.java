package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.model.User;
import ru.ylab.repository.AuthRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthRepository authRepository;

    @Test
    @DisplayName(value = "Тест успешной регистрации пользователя")
    @SneakyThrows
    void registerUserTest() {
        String expected = "name";

        when(authRepository.ifExistUser("name")).thenReturn(false);
        when(authRepository.registerUser("name", "pass")).thenReturn("name");

        String actual = authService.registerUser("name", "pass");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Тест регистрации уже зарегистрированного пользователя")
    @SneakyThrows
    void registerUserTest_whenAlreadyRegistered() {
        when(authRepository.ifExistUser("name")).thenReturn(true);

        authService.registerUser("name", "pass");

        assertThat(authService.registerUser("name", "p")).isNull();
    }

    @Test
    @DisplayName(value = "Тест авторизации пользователя")
    @SneakyThrows
    void authUserTest() {
        when(authRepository.getUser("name")).thenReturn(new User("name", "p"));

        assertThat(authService.authUser("name", "p")).isEqualTo("name");
    }

    @Test
    @DisplayName(value = "Тест авторизации пользователя с неверным паролем")
    @SneakyThrows
    void authUserTest_whenPasswordsNotMatch() {
        when(authRepository.getUser("name")).thenReturn(new User("name", "p"));

        assertThat(authService.authUser("name", "pass")).isNull();
    }

    @Test
    @DisplayName(value = "Тест авторизации незарегистрированного пользователя")
    @SneakyThrows
    void authUserTest_whenNotRegistered() {
        when(authRepository.getUser("name")).thenReturn(null);

        assertThat(authService.authUser("name", "p")).isNull();
    }
}
