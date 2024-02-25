package ru.ylab.service;

import ru.ylab.domain.model.Indication;

import java.util.List;

/**
 * Monitoring service is responsible for business logic.
 */
public interface MonitoringService {

    /**
     * Send indication.
     *
     * @param indication the indication
     */
    Indication sendIndication(Indication indication);

    /**
     * Check indication for the month.
     *
     * @param username the username
     * @param type     id of the type of the indication
     * @param month    the month
     * @return the value of indication
     */
    Indication getIndicationForMonth(long type, String username, int month);

    /**
     * Get the last indication.
     *
     * @param type     id of the type of the indication
     * @param username the username
     * @return the value
     */
    Indication getLastIndication(long type, String username);

    /**
     * Gets all indications.
     *
     * @return all indications values
     */
    List<Indication> getAllIndications();

    /**
     * Gets the history of all user's indications.
     *
     * @param username the username
     * @return all user's indications values
     */
    List<Indication> getAllIndicationsOfUser(String username);
}
