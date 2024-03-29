package ru.ylab;

import ru.ylab.in.MonitoringController;
import ru.ylab.service.AuthService;
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
        MonitoringController monitoringController = new MonitoringController(new Scanner(System.in),
                new MonitoringService(), new AuthService());
        monitoringController.distributeRoles();
    }
}