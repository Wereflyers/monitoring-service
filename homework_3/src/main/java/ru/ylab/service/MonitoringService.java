package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.exceptions.SomeSQLException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.Indication;
import ru.ylab.model.IndicationType;
import ru.ylab.repository.MonitoringRepository;
import ru.ylab.repository.MonitoringRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Monitoring service is responsible for business logic.
 */
@Loggable
@RequiredArgsConstructor
public class MonitoringService {
    /**
     * Injection of Repository.
     */
    private final MonitoringRepository monitoringRepository;
    /**
     * Injection of Type Service.
     */
    private final IndicationTypeService indicationTypeService;

    /**
     * Instantiates a new Monitoring service.
     */
    public MonitoringService() {
        this.monitoringRepository = new MonitoringRepositoryImpl();
        this.indicationTypeService = new IndicationTypeService();
    }

    /**
     * Send indication.
     *
     * @param indication the indication
     */
    public Indication sendIndication(Indication indication) {
        try {
            LocalDate date = LocalDate.now();
            IndicationType indicationType = indicationTypeService.getType(indication.getType());
            if (indicationType == null) {
                throw new WrongDataException("Некорректный тип показания");
            }

            Indication lastIndication = monitoringRepository.getLastIndication(indicationType.getId(),
                    indication.getUsername());
            if (lastIndication != null) {
                int newIndicationYear = date.getYear();
                Month newIndicationMonth = date.getMonth();
                if (lastIndication.getDate().getYear() == newIndicationYear
                        && lastIndication.getDate().getMonth() == newIndicationMonth) {
                    throw new WrongDataException("Данные за этот месяц уже были введены.");
                }
                if (lastIndication.getValue() > indication.getValue()) {
                    throw new WrongDataException("Введенное число меньше последних показаний. Ввведите корректное число");
                }
            }

            monitoringRepository.sendIndication(indication, indicationType.getId());
            return monitoringRepository.getLastIndication(indicationType.getId(), indication.getUsername());
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    /**
     * Check indication for the month.
     *
     * @param username the username
     * @param type     id of the type of the indication
     * @param month    the month
     * @return the value of indication
     */
    public Long checkIndicationForMonth(String username, long type, int month) {
        try {
            return monitoringRepository.checkIndicationForMonth(username, type, month);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    /**
     * Get the last indication.
     *
     * @param type     id of the type of the indication
     * @param username the username
     * @return the value
     */
    public Indication getLastIndication(long type, String username) {
        try {
            return monitoringRepository.getLastIndication(type, username);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    /**
     * Gets all indications.
     *
     * @return all indications values
     */
    public List<Indication> getAllIndications() {
        try {
            return monitoringRepository.getAllIndications();
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    /**
     * Gets the history of all user's indications.
     *
     * @param username the username
     * @return all user's indications values
     */
    public List<Indication> getAllIndicationsOfUser(String username) {
        try {
            return monitoringRepository.getAllIndicationsOfUser(username);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }
}
