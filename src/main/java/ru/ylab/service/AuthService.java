package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.model.User;
import ru.ylab.repository.AuthRepository;

import java.sql.SQLException;

/**
 * AuthService is responsible for registration and authorization
 */
@RequiredArgsConstructor
public class AuthService {
    /**
     * Repository with users.
     */
    private final AuthRepository authRepository;

    /**
     * Register user.
     *
     * @param username the username
     * @param password the password
     * @return username
     */
    public String registerUser(String username, String password) {
        try {
            if (authRepository.ifExistUser(username)) {
                System.out.println("Такой пользователь уже зарегистрирован");
                return null;
            }
            return authRepository.registerUser(username, password);
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Auth user.
     *
     * @param username the username
     * @param password the password
     * @return username
     */
    public String authUser(String username, String password) {
        try {
            User savedUser = authRepository.getUser(username);
            if (savedUser == null) {
                System.out.println("Пользователя с таким именем не существует");
                return null;
            }
            if (!password.equals(savedUser.getPassword())) {
                System.out.println("Введен некорректный пароль");
                return null;
            }
            System.out.println("Пользователь " + username + " вошел в сеть.");
            return username;
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }
}
