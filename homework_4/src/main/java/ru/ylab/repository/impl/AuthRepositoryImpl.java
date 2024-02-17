package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ylab.config.JDBCConfig;
import ru.ylab.domain.model.User;
import ru.ylab.repository.AuthRepository;

import java.sql.*;

/**
 * Implementation with JDBC
 */
@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
    private final JDBCConfig config;

    public AuthRepositoryImpl() {
        this.config = new JDBCConfig();
    }

    @Override
    public String registerUser(String username, String password) throws SQLException {
        try (Connection connection = config.connect()) {
            String insertDataSql = "INSERT INTO users_table (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return username;
        }
    }

    @Override
    public boolean hasUser(String username) throws SQLException {
        try (Connection connection = config.connect()) {
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
    }

    @Override
    public User getUser(String username) throws SQLException {
        try (Connection connection = config.connect()) {
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

    public void deleteAll() throws SQLException {
        try (Connection connection = config.connect()) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM users_table";
            int deletedRows = statement.executeUpdate(query);
            if (deletedRows > 0) {
                System.out.println("Deleted All Rows In The Table Successfully...");
            } else {
                System.out.println("Table already empty.");
            }
        }
    }
}
