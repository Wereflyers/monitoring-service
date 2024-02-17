package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ylab.config.JDBCConfig;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.repository.IndicationTypeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository with types of indications
 */
@Repository
@RequiredArgsConstructor
public class IndicationTypeRepositoryImpl implements IndicationTypeRepository {
    private final JDBCConfig config;

    public IndicationTypeRepositoryImpl() {
        this.config = new JDBCConfig();
    }

    @Override
    public IndicationType getTypeByName(String typeName) throws SQLException {
        try (Connection connection = config.connect()) {
            String checkDataSql = "SELECT * FROM indication_type WHERE type_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
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
        try (Connection connection = config.connect()) {
            String checkDataSql = "SELECT * FROM INDICATION_TYPE";
            Statement statement = connection.createStatement();
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
        try (Connection connection = config.connect()) {
            String insertDataSql = "INSERT INTO indication_type (TYPE_NAME) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
            preparedStatement.setString(1, type.toUpperCase());
            preparedStatement.executeUpdate();
        }
    }

    public void delete(String type) throws SQLException {
        try (Connection connection = config.connect()) {
            String query = "DELETE FROM indication_type WHERE type_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, type.toUpperCase());
            int deletedRows = statement.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Deleted All Rows In The Table Successfully...");
            } else {
                System.out.println("Table already empty.");
            }
        }
    }
}
