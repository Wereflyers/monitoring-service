package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.ylab.domain.model.User;
import ru.ylab.repository.AuthRepository;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Implementation with JDBC
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthRepositoryImpl implements AuthRepository {
    private final DataSource dataSource;

    @Override
    public String registerUser(String username, String password) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String insertDataSql = "INSERT INTO users_table (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = getConnection.prepareStatement(insertDataSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return username;
        }
    }

    @Override
    public boolean hasUser(String username) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT " +
                    "EXISTS(SELECT * FROM users_table WHERE username = ?)";
            PreparedStatement preparedStatement = getConnection.prepareStatement(checkDataSql);
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
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM users_table WHERE username = ?";
            PreparedStatement preparedStatement = getConnection.prepareStatement(checkDataSql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                Long savedId = resultSet.getLong("id");
                String savedName = resultSet.getString("username");
                String savedPassword = resultSet.getString("password");
                user = new User(savedId, savedName, savedPassword);
            }
            return user;
        }
    }

    public void deleteAll() throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            Statement statement = getConnection.createStatement();
            String query = "DELETE FROM users_table";
            int deletedRows = statement.executeUpdate(query);
            if (deletedRows > 0) {
                log.info("Deleted All Rows In The Table Successfully...");
            } else {
                log.info("Table already empty.");
            }
        }
    }
}
