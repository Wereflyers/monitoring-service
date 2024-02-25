package ru.ylab.repository.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.ylab.domain.model.Indication;
import ru.ylab.repository.MonitoringRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Monitoring repo is responsible for getConnection to the database.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MonitoringRepositoryImpl implements MonitoringRepository {
    private final DataSource dataSource;

    @Override
    public void sendIndication(Indication indication, long type) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String insertDataSql = "INSERT INTO INDICATION (USERNAME, INDICATION_VALUE, DATE, TYPE) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = getConnection.prepareStatement(insertDataSql);
            preparedStatement.setString(1, indication.getUsername());
            preparedStatement.setLong(2, indication.getValue());
            preparedStatement.setDate(3, Date.valueOf(indication.getDate()));
            preparedStatement.setLong(4, type);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Indication checkIndicationForMonth(String username, long type, int month) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM INDICATION I " +
                    "LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID " +
                    "WHERE TYPE = ? and USERNAME = ? " +
                    "and date_part('month', DATE) = ? and date_part('year', DATE) = ?";
            PreparedStatement preparedStatement = getConnection.prepareStatement(checkDataSql);
            preparedStatement.setLong(1, type);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, month);
            preparedStatement.setInt(4, LocalDate.now().getYear());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? getIndicationFromRow(resultSet) : null;
        }
    }

    @Override
    public Indication getLastIndication(long typeId, String username) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM INDICATION I " +
                    "LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID " +
                    "WHERE TYPE = ? and USERNAME = ? " +
                    "ORDER BY DATE LIMIT 1";
            PreparedStatement preparedStatement = getConnection.prepareCall(checkDataSql);
            preparedStatement.setLong(1, typeId);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? getIndicationFromRow(resultSet) : null;
        }
    }

    @Override
    public List<Indication> getAllIndications() throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM INDICATION I LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID";
            Statement statement = getConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkDataSql);
            List<Indication> indicationList = new ArrayList<>();
            while (resultSet.next()) {
                indicationList.add(getIndicationFromRow(resultSet));
            }
            return indicationList;
        }
    }

    @Override
    public List<Indication> getAllIndicationsOfUser(String username) throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            String checkDataSql = "SELECT * FROM INDICATION I LEFT JOIN INDICATION_TYPE IT ON I.TYPE = IT.ID " +
                    "WHERE USERNAME = ?";
            PreparedStatement statement = getConnection.prepareStatement(checkDataSql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            List<Indication> indicationList = new ArrayList<>();
            while (resultSet.next()) {
                indicationList.add(getIndicationFromRow(resultSet));
            }
            return indicationList;
        }
    }

    private Indication getIndicationFromRow(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long value = resultSet.getLong("indication_value");
        String type = resultSet.getString("type_name");
        String username = resultSet.getString("username");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        return new Indication(id, type, date, value, username);
    }

    public void deleteAll() throws SQLException {
        try (Connection getConnection = dataSource.getConnection()) {
            Statement statement = getConnection.createStatement();
            String query = "DELETE FROM indication";
            int deletedRows = statement.executeUpdate(query);
            if (deletedRows > 0) {
                log.info("Deleted All Rows In The Table Successfully...");
            } else {
                log.info("Table already empty.");
            }
        }
    }
}
