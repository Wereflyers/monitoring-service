package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.domain.model.Indication;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.exceptions.SomeSQLException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.repository.MonitoringRepository;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Monitoring service is responsible for business logic.
 */
@Loggable
@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {
    /**
     * Injection of Repository.
     */
    private final MonitoringRepository monitoringRepository;
    /**
     * Injection of Type Service.
     */
    private final IndicationTypeService indicationTypeService;

    @Override
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

    @Override
    public Indication getIndicationForMonth(long type, String username, int month) {
        try {
            return monitoringRepository.checkIndicationForMonth(username, type, month);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public Indication getLastIndication(long type, String username) {
        try {
            return monitoringRepository.getLastIndication(type, username);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public List<Indication> getAllIndications() {
        try {
            return monitoringRepository.getAllIndications();
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public List<Indication> getAllIndicationsOfUser(String username) {
        try {
            return monitoringRepository.getAllIndicationsOfUser(username);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }
}
