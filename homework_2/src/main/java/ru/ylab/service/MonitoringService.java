package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.model.Indication;
import ru.ylab.repository.MonitoringRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

/**
 * Monitoring service is responsible for business logic.
 */
@RequiredArgsConstructor
public class MonitoringService {
    /**
     * Injection of Repository.
     */
    private final MonitoringRepository monitoringRepository;

    /**
     * Send indication.
     *
     * @param typeId id of the type of the indication
     * @param username the username
     * @param date     the date of indication
     * @param value    value of the indication
     */
    public void sendIndication(long typeId, String username, LocalDate date, Long value) {
        try {
            Indication lastIndication = monitoringRepository.getLastIndication(typeId, username);
            if (lastIndication != null) {
                int newIndicationYear = date.getYear();
                Month newIndicationMonth = date.getMonth();
                if (lastIndication.getDate().getYear() == newIndicationYear
                        && lastIndication.getDate().getMonth() == newIndicationMonth) {
                    System.out.println("Данные за этот месяц уже были введены.");
                    return;
                }
                if (lastIndication.getValue() > value) {
                    System.out.println("Введенное число меньше последних показаний. Ввведите корректное число");
                    return;
                }
            }
            monitoringRepository.sendIndication(username, new Indication(date, value), typeId);
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
        }
    }

    /**
     * Check indication for the month.
     *
     * @param username the username
     * @param type id of the type of the indication
     * @param month    the month
     * @return the value of indication
     */
    public Long checkIndicationForMonth(String username, long type, int month) {
        try {
            return monitoringRepository.checkIndicationForMonth(username, type, month);
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the last indication.
     *
     * @param type id of the type of the indication
     * @param username the username
     * @return the value
     */
    public String checkLastIndicationAmount(long type, String username) {
        try {
            Indication lastIndication = monitoringRepository.getLastIndication(type, username);
            if (lastIndication == null) {
                return "Нет введенных показаний";
            }
            return String.valueOf(lastIndication.getValue());
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all indications.
     *
     * @return all indications values
     */
    public String getAllIndications() {
        try {
            List<Indication> indications = monitoringRepository.getAllIndications();
            StringBuilder sb = new StringBuilder();
            if (indications != null) {
                indications.stream()
                        .sorted(Comparator.comparing(Indication::getUsername))
                        .forEach(i -> sb.append(i).append(System.lineSeparator()));
            }
            return sb.toString();
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the history of all user's indications.
     *
     * @param username the username
     * @return all user's indications values
     */
    public String getAllIndicationsOfUser(String username) {
        try {
            List<Indication> userIndications = monitoringRepository.getAllIndicationsOfUser(username);
            if (userIndications != null) {
                StringBuilder sb = new StringBuilder();
                userIndications.forEach(i -> sb.append(i).append(System.lineSeparator()));
                return sb.toString();
            }
            return "Нет введенных показаний";
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }
}
