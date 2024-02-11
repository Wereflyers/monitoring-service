package ru.ylab;

import ru.ylab.config.JDBCConfig;
import ru.ylab.in.MonitoringController;
import ru.ylab.repository.AuthRepositoryImpl;
import ru.ylab.repository.IndicationTypeRepositoryImpl;
import ru.ylab.repository.MonitoringRepositoryImpl;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.util.Scanner;

/**
 * The main class of Monitoring service.
 *
 * @author Ann
 */
public class MonitoringApp {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        JDBCConfig.doConnect();
        MonitoringController monitoringController = new MonitoringController(new MonitoringService(
                new MonitoringRepositoryImpl()), new AuthService(new AuthRepositoryImpl()),
                new IndicationTypeService(new IndicationTypeRepositoryImpl()),
                new Scanner(System.in));
        monitoringController.distributeRoles();
    }
}