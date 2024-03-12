package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.repository.IndicationTypeRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository with types of indications
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class IndicationTypeRepositoryImpl implements IndicationTypeRepository {
    private final DataSource dataSource;

    @Override
    public IndicationType getTypeByName(String typeName) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM indication_type WHERE type_name = ?";
            PreparedStatement preparedStatement = getConnection.prepareStatement(checkDataSql);
            preparedStatement.setString(1, typeName.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("type_name");
                return new IndicationType(id, name);
            }
            return null;
        }
    }

    @Override
    public List<String> getAllTypes() throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM INDICATION_TYPE";
            Statement statement = getConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkDataSql);
            List<String> indicationTypeList = new ArrayList<>();
            while (resultSet.next()) {
                indicationTypeList.add(resultSet.getString("type_name"));
            }
            return indicationTypeList;
        }
    }

    @Override
    public void addType(String type) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String insertDataSql = "INSERT INTO indication_type (TYPE_NAME) VALUES (?)";
            PreparedStatement preparedStatement = getConnection.prepareStatement(insertDataSql);
            preparedStatement.setString(1, type.toUpperCase());
            preparedStatement.executeUpdate();
        }
    }

    public void delete(String type) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String query = "DELETE FROM indication_type WHERE type_name = ?";
            PreparedStatement statement = getConnection.prepareStatement(query);
            statement.setString(1, type.toUpperCase());
            int deletedRows = statement.executeUpdate();
            if (deletedRows > 0) {
                log.info("Deleted All Rows In The Table Successfully...");
            } else {
                log.info("Table already empty.");
            }
        }
    }
}
