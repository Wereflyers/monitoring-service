package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.domain.model.User;
import ru.ylab.exceptions.UserAlreadyRegisteredException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.repository.AuthRepository;
import ru.ylab.service.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private AuthRepository authRepository;

    @Test
    @DisplayName(value = "Тест успешной регистрации пользователя")
    @SneakyThrows
    void registerUserTest() {
        String expected = "name";

        when(authRepository.hasUser("name")).thenReturn(false);
        when(authRepository.registerUser("name", "pass")).thenReturn("name");

        String actual = authServiceImpl.registerUser(new User(1L, "name", "pass"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Тест регистрации уже зарегистрированного пользователя")
    @SneakyThrows
    void registerUserTest_whenAlreadyRegistered() {
        when(authRepository.hasUser("name")).thenReturn(true);

        assertThatThrownBy(() -> authServiceImpl.registerUser(new User(1L, "name", "pass")))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    @DisplayName(value = "Тест авторизации пользователя")
    @SneakyThrows
    void authUserTest() {
        when(authRepository.getUser("name")).thenReturn(new User(1L, "name", "p"));

        assertThatCode(() -> authServiceImpl.authUser(new User(1L, "name", "p")))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName(value = "Тест авторизации пользователя с неверным паролем")
    @SneakyThrows
    void authUserTest_whenPasswordsNotMatch() {
        when(authRepository.getUser("name")).thenReturn(new User(1L, "name", "p"));

        assertThatThrownBy(() -> authServiceImpl.authUser(new User(1L, "name", "pass")))
                .isInstanceOf(WrongDataException.class);
    }

    @Test
    @DisplayName(value = "Тест авторизации незарегистрированного пользователя")
    @SneakyThrows
    void authUserTest_whenNotRegistered() {
        when(authRepository.getUser("name")).thenReturn(null);

        assertThatThrownBy(() -> authServiceImpl.authUser(new User(1L, "name", "pass")))
                .isInstanceOf(WrongDataException.class);
    }
}
