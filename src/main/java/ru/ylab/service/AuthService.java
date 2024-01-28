package ru.ylab.service;

import ru.ylab.exception.AccessDenialException;

import java.util.HashMap;

/**
 * AuthService is responsible for registration and authorization
 */
public class AuthService {
    /**
     * HashMap<K, V> is a collection of users
     * K - username
     * V - password
     */
    private final HashMap<String, String> authDetails;

    /**
     * Instantiates a new Auth service.
     */
    public AuthService() {
        authDetails = new HashMap<>();
        authDetails.put("admin", "PASSWORD123");
    }

    /**
     * Register user.
     *
     * @param username the username
     * @param password the password
     */
    public void registerUser(String username, String password) {
        if (authDetails.containsKey(username))
            throw new AccessDenialException("Такой пользователь уже зарегистрирован");
        authDetails.put(username, password);
        System.out.println("Пользователь успешно зарегистрирован");
    }

    /**
     * Auth user.
     *
     * @param username the username
     * @param password the password
     */
    public void authUser(String username, String password) {
        if (!authDetails.containsKey(username)) {
            throw new AccessDenialException("Пользователя с таким именем не существует");
        }
        String savedPassword = authDetails.get(username);
        if (!savedPassword.equals(password))
            throw new AccessDenialException("Введен некорректный пароль");
        System.out.println("Пользователь " + username + " вошел в сеть.");
    }
}
