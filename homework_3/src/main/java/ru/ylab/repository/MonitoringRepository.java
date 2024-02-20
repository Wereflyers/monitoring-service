package ru.ylab.repository;


import ru.ylab.model.Indication;

import java.sql.SQLException;
import java.util.List;

/**
 * The interface Monitoring repository.
 */
public interface MonitoringRepository {
    /**
     * Send indication.
     *
     * @param indication the indication
     * @param type       the type
     * @throws SQLException the sql exception
     */
    void sendIndication(Indication indication, long type) throws SQLException;

    /**
     * Check indication for the month.
     *
     * @param username the username
     * @param type     the type
     * @param month    the month
     * @return the value of indication
     * @throws SQLException the sql exception
     */
    Long checkIndicationForMonth(String username, long type, int month) throws SQLException;

    /**
     * Get the last indication
     *
     * @param typeId   String
     * @param username String
     * @return indication of a known type or null
     * @throws SQLException the sql exception
     */
    Indication getLastIndication(long typeId, String username) throws SQLException;

    /**
     * Gets all indications.
     *
     * @return all indications values
     * @throws SQLException the sql exception
     */
    List<Indication> getAllIndications() throws SQLException;

    /**
     * Gets the history of all user's indications.
     *
     * @param username the username
     * @return all user's indications values
     * @throws SQLException the sql exception
     */
    List<Indication> getAllIndicationsOfUser(String username) throws SQLException;
}
