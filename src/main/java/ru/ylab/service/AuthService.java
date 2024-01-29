package ru.ylab.service;

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
     * @return username
     */
    public String registerUser(String username, String password) {
        if (authDetails.containsKey(username)) {
            System.out.println("Такой пользователь уже зарегистрирован");
            return null;
        }
        authDetails.put(username, password);
        System.out.println("Пользователь успешно зарегистрирован");
        return username;
    }

    /**
     * Auth user.
     *
     * @param username the username
     * @param password the password
     * @return username
     */
    public String authUser(String username, String password) {
        if (!authDetails.containsKey(username)) {
            System.out.println("Пользователя с таким именем не существует");
            return null;
        }
        String savedPassword = authDetails.get(username);
        if (!savedPassword.equals(password)) {
            System.out.println("Введен некорректный пароль");
            return null;
        }
        System.out.println("Пользователь " + username + " вошел в сеть.");
        return username;
    }
}
