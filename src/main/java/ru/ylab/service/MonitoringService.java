package ru.ylab.service;

import ru.ylab.dto.Indication;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Monitoring service is responsible for business logic.
 */
public class MonitoringService {
    /**
     * HashMap<K, V> with all sent indications
     * K - username
     * V - indications
     */
    private final HashMap<String, ArrayDeque<Indication>> indications = new HashMap<>();

    /**
     * Send indication.
     *
     * @param username   the username
     * @param date       the date of indication
     * @param value value of the indication
     */
    public void sendIndication(String type, String username, LocalDate date, Long value) {
        ArrayDeque<Indication> userIndications = indications.get(username);
        if (userIndications == null) {
            userIndications = new ArrayDeque<>();
        } else if (!userIndications.isEmpty()) {
            Indication indication = getLastIndication(type, username);
            if (indication != null) {
                if (indication.getDate().getYear() == date.getYear() && indication.getDate().getMonth() == date.getMonth()) {
                    System.out.println("Данные за этот месяц уже были введены.");
                    return;
                }
                if (indication.getValue() > value) {
                    System.out.println("Введенное число меньше последних показаний. Ввведите корректное число");
                    return;
                }
            }
        }
        userIndications.add(new Indication(type, date, value));
        indications.put(username, userIndications);
    }

    /**
     * Check indication for the month.
     *
     * @param username the username
     * @param month    the month
     * @return the value of indication
     */
    public Long checkIndicationForMonth(String username, int month) {
        ArrayDeque<Indication> userIndications = indications.get(username);
        if (userIndications != null) {
            for (Indication indication : userIndications) {
                if (indication.getDate().getYear() == LocalDate.now().getYear()
                        && indication.getDate().getMonth() == Month.of(month))
                    return indication.getValue();
            }
        }
        System.out.println("Нет показаний за этот месяц");
        return null;
    }

    /**
     * Get the last indication.
     *
     * @param username the username
     * @return the value
     */
    public String checkLastIndicationAmount(String indicationType, String username) {
        Indication lastIndication = getLastIndication(indicationType, username);
        if (lastIndication == null) return "Нет введенных показаний";
        return String.valueOf(lastIndication.getValue());
    }

    /**
     * Get the last indication
     *
     * @param indicationType String
     * @param username String
     * @return indication of a known type or null
     */
    private Indication getLastIndication(String indicationType, String username) {
        ArrayDeque<Indication> userIndications = indications.get(username).clone();
        if (userIndications == null) return null;
        Indication lastIndication = userIndications.pollLast();
        while (lastIndication != null && !lastIndication.getType().equals(indicationType)) {
            lastIndication = userIndications.pollLast();
        }
        return lastIndication;
    }

    /**
     * Gets all indications.
     *
     * @return all indications values
     */
    public String getAllIndications() {
        StringBuilder sb = new StringBuilder();
        for (String user : indications.keySet()) {
            sb.append(user).append(System.lineSeparator());
            sb.append(getAllIndicationsOfUser(user));
        }
        return sb.toString();
    }

    /**
     * Gets the history of all user's indications.
     *
     * @param username the username
     * @return all user's indications values
     */
    public String getAllIndicationsOfUser(String username) {
        ArrayDeque<Indication> userIndications = indications.get(username);
        if (userIndications != null) {
            StringBuilder sb = new StringBuilder();
            for (Indication indication : userIndications) {
                sb.append(indication.getType()).append(" ").append(indication.getDate()).append(" - ")
                        .append(indication.getValue()).append(System.lineSeparator());
            }
            return sb.toString();
        }
        return "Нет введенных показаний";
    }
}
