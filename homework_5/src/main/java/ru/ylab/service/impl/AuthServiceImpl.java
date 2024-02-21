package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.domain.model.User;
import ru.ylab.exceptions.DBException;
import ru.ylab.exceptions.UserAlreadyRegisteredException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.repository.AuthRepository;
import ru.ylab.service.AuthService;

import java.sql.SQLException;

@Loggable
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    /**
     * Repository with users.
     */
    private final AuthRepository authRepository;

    @Override
    public String registerUser(User user) {
        try {
            if (authRepository.hasUser(user.getUsername())) {
                throw new UserAlreadyRegisteredException("Такой пользователь уже зарегистрирован");
            }
            return authRepository.registerUser(user.getUsername(), user.getPassword());
        } catch (SQLException e) {
            throw new DBException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public void authUser(User user) {
        try {
            User savedUser = authRepository.getUser(user.getUsername());
            if (savedUser == null) {
                throw new WrongDataException("Пользователя с таким именем не существует");
            }
            if (!user.getPassword().equals(savedUser.getPassword())) {
                throw new WrongDataException("Введен некорректный пароль");
            }
            log.info("Пользователь " + user.getUsername() + " вошел в сеть.");
        } catch (SQLException e) {
            throw new DBException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public boolean hasUser(String name) {
        try {
            return authRepository.hasUser(name);
        } catch (SQLException e) {
            throw new DBException("SQLException occurred " + e.getSQLState());
        }
    }
}
