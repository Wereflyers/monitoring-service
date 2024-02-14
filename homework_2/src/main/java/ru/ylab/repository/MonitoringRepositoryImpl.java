package ru.ylab.repository;


import lombok.RequiredArgsConstructor;
import ru.ylab.config.JDBCConfig;
import ru.ylab.model.Indication;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Monitoring repo is responsible for connection to the database.
 */
@RequiredArgsConstructor
public class MonitoringRepositoryImpl implements MonitoringRepository {

    /**
     * Connection to a database
     */
    private final Connection connection;

    /**
     * Instantiates a new Auth repository.
     */
    public MonitoringRepositoryImpl() {
        connection = JDBCConfig.connect();
    }

    @Override
    public void sendIndication(String username, Indication indication, long type) throws SQLException {
        String insertDataSql = "INSERT INTO INDICATION (USERNAME, INDICATION_VALUE, DATE, TYPE) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, username);
        preparedStatement.setLong(2, indication.getValue());
        preparedStatement.setDate(3, Date.valueOf(indication.getDate()));
        preparedStatement.setLong(4, type);
        preparedStatement.executeUpdate();
    }

    @Override
    public Long checkIndicationForMonth(String username, long type, int month)
            throws SQLException {
        String checkDataSql = "SELECT * FROM INDICATION WHERE TYPE = ? and USERNAME = ? " +
                "and date_part('month', DATE) = ? and date_part('year', DATE) = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
        preparedStatement.setLong(1, type);
        preparedStatement.setString(2, username);
        preparedStatement.setInt(3, month);
        preparedStatement.setInt(4, LocalDate.now().getYear());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong("indication_value");
        }
        return null;
    }

    @Override
    public Indication getLastIndication(long typeId, String username) throws SQLException {
        String checkDataSql = "SELECT * FROM INDICATION I " +
                "LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID " +
                "WHERE TYPE = ? and USERNAME = ? ";
        CallableStatement callableStatement = connection.prepareCall(checkDataSql, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        callableStatement.setLong(1, typeId);
        callableStatement.setString(2, username);
        ResultSet resultSet = callableStatement.executeQuery();
        boolean hasData = resultSet.last();
        return hasData ? getIndicationFromRow(resultSet) : null;
    }

    @Override
    public List<Indication> getAllIndications() throws SQLException {
        String checkDataSql = "SELECT * FROM INDICATION I LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID";
        PreparedStatement preparedStatement = connection.prepareStatement(checkDataSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Indication> indicationList = new ArrayList<>();
        while (resultSet.next()) {
            indicationList.add(getIndicationFromRow(resultSet));
        }
        return indicationList;
    }

    @Override
    public List<Indication> getAllIndicationsOfUser(String username) throws SQLException {
        String checkDataSql = "SELECT * FROM INDICATION I LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID " +
                "WHERE USERNAME = ?";
        PreparedStatement statement = connection.prepareStatement(checkDataSql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        List<Indication> indicationList = new ArrayList<>();
        while (resultSet.next()) {
            indicationList.add(getIndicationFromRow(resultSet));
        }
        return indicationList;
    }

    private Indication getIndicationFromRow(ResultSet resultSet) throws SQLException {
        long value = resultSet.getLong("indication_value");
        String type = resultSet.getString("type_name");
        String username = resultSet.getString("username");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        return new Indication(type, date, value, username);
    }
}
