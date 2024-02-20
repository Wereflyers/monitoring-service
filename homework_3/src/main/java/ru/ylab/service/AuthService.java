package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.exceptions.SomeSQLException;
import ru.ylab.exceptions.UserAlreadyRegisteredException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.User;
import ru.ylab.repository.AuthRepository;
import ru.ylab.repository.AuthRepositoryImpl;

import java.sql.SQLException;

/**
 * AuthService is responsible for registration and authorization
 */
@Loggable
@RequiredArgsConstructor
public class AuthService {
    /**
     * Repository with users.
     */
    private final AuthRepository authRepository;

    /**
     * Instantiates a new Auth service.
     */
    public AuthService() {
        this.authRepository = new AuthRepositoryImpl();
    }

    /**
     * Register user.
     *
     * @param user the user
     * @return username string
     */
    public String registerUser(User user) {
        try {
            if (authRepository.ifExistUser(user.getUsername())) {
                throw new UserAlreadyRegisteredException("Такой пользователь уже зарегистрирован");
            }
            return authRepository.registerUser(user.getUsername(), user.getPassword());
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    /**
     * Auth user.
     *
     * @param user the user
     */
    public void authUser(User user) {
        try {
            User savedUser = authRepository.getUser(user.getUsername());
            if (savedUser == null) {
                throw new WrongDataException("Пользователя с таким именем не существует");
            }
            if (!user.getPassword().equals(savedUser.getPassword())) {
                throw new WrongDataException("Введен некорректный пароль");
            }
            System.out.println("Пользователь " + user.getUsername() + " вошел в сеть.");
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
        }
    }

    /**
     * Is user exist boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isUserExist(String name) {
        try {
            return authRepository.ifExistUser(name);
        } catch (SQLException e) {
            return false;
        }
    }
}
