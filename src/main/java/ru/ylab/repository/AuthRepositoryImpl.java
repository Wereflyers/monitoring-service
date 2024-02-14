package ru.ylab.repository;

import ru.ylab.config.JDBCConfig;
import ru.ylab.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation with JDBC
 */
public class AuthRepositoryImpl implements AuthRepository {

    /**
     * Connection to a database
     */
    private final Connection connection;

    /**
     * Instantiates a new Auth repository.
     */
    public AuthRepositoryImpl() {
        connection = JDBCConfig.connect();
    }

    @Override
    public String registerUser(String username, String password) throws SQLException {
        String insertDataSql = "INSERT INTO users_table (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
        return username;
    }

    @Override
    public boolean ifExistUser(String username) throws SQLException {
        String checkDataSql = "SELECT " +
                "EXISTS(SELECT * FROM users_table WHERE username = ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        if (resultSet.next()) {
            result = resultSet.getString(1);
        }
        return result.equals("t");
    }

    @Override
    public User getUser(String username) throws SQLException {
        String checkDataSql = "SELECT * FROM users_table WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            String savedName = resultSet.getString("username");
            String savedPassword = resultSet.getString("password");
            user = new User(savedName, savedPassword);
        }
        return user;
    }
}
