package ru.ylab.repository;

import ru.ylab.config.JDBCConfig;
import ru.ylab.model.IndicationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository with types of indications
 */
public class IndicationTypeRepositoryImpl implements IndicationTypeRepository {
    /**
     * Connection to a database
     */
    private final Connection connection;

    /**
     * Instantiates a new Indication type repository.
     */
    public IndicationTypeRepositoryImpl() {
        connection = JDBCConfig.connect();
    }

    @Override
    public IndicationType getTypeByName(String typeName) throws SQLException {
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

    @Override
    public List<String> getAllTypes() throws SQLException {
        String checkDataSql = "SELECT * FROM INDICATION_TYPE";
        PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> indicationTypeList = new ArrayList<>();
        while (resultSet.next()) {
            indicationTypeList.add(resultSet.getString("type_name"));
        }
        return indicationTypeList;
    }

    @Override
    public void addType(String type) throws SQLException {
        String insertDataSql = "INSERT INTO indication_type (TYPE_NAME) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, type.toUpperCase());
        preparedStatement.executeUpdate();
    }
}
