package ru.ylab.in;

import ru.ylab.dto.IndicationType;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MonitoringController is responsible for receiving requests from the console
 */
public class MonitoringController {
    /**
     * Monitoring Service Exemplar
     */
    private final MonitoringService monitoringService;
    /**
     * Auth Service Exemplar
     */
    private final AuthService authService;
    /**
     * List of all users' actions
     */
    private final List<String> userAuditTrail;
    /**
     * Scan from the console
     */
    private final Scanner scanner;

    /**
     * Instantiates a new Distributive service, initializes all variables.
     */
    public MonitoringController(Scanner scanner, MonitoringService monitoringService, AuthService authService) {
        this.monitoringService = monitoringService;
        this.authService = authService;
        this.userAuditTrail = new ArrayList<>();
        this.scanner = scanner;
    }

    /**
     * Distribute requests of admin and user.
     */
    public void distributeRoles() {
        String name = null;

        while (name == null) {
            name = getAuthenticationSteps();
        }

        while (name != null) {
            name = (name.equals("admin")) ? printAdminActions() : printUserActions(name);
        }
    }

    /**
     * Print available actions for user and call methods of MonitoringService to do them.
     * If the user logs out of the account - returns the name of the new user.
     * If the user wants to terminate the application - returns null
     *
     * @return new username or null
     */
    private String printUserActions(String name) {
        while (true) {
            int step = getNextUserStep();

            switch (step) {
                case 1:
                    addIndication(name);
                    break;
                case 2:
                    String indicationType = getIndicationType();
                    System.out.println(monitoringService.checkLastIndicationAmount(indicationType, name));
                    userAuditTrail.add("Пользователем " + name + " просмотрено последнее показание показание счетчика "
                    + indicationType);
                    break;
                case 3:
                    getIndicationForMonth(name);
                    break;
                case 4:
                    System.out.println(monitoringService.getAllIndicationsOfUser(name));
                    userAuditTrail.add("Пользователем " + name + " просмотрены все переданные показания");
                    break;
                case 5:
                    userAuditTrail.add("Пользователь " + name + " - выход из учетной записи");
                    return getAuthenticationSteps();
                case 6:
                    return null;
                default:
                    System.out.println("Введите корректное число");
            }
        }
    }

    private void getIndicationForMonth(String name) {
        int month;
        while (true) {
            try {
                System.out.println("За какой месяц вы хотите посмотреть показания? Введите число");
                month = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ыыедите корректное число");
            }
        }
        String indicationType = getIndicationType();
        System.out.println(monitoringService.checkIndicationForMonth(name, indicationType, month));
        userAuditTrail.add("Пользователем " + name +
                " просмотрено последнее показание показание счетчика за месяц " + month);
    }

    /**
     * Add an indication by user
     *
     * @param name username
     */
    private void addIndication(String name) {
        String indicationType = getIndicationType();
        System.out.println("Введите показания (целое число)");
        long value = Long.parseLong(scanner.nextLine());
        monitoringService.sendIndication(indicationType, name, LocalDate.now(), value);
        userAuditTrail.add("Пользователем " + name + " отправлено показание счетчика" + value);
    }

    /**
     * Get a type of indication from the console
     *
     * @return indication type
     */
    private String getIndicationType() {
        while (true) {
            System.out.println("Введите тип показания " + IndicationType.types);
            String indicationType = scanner.nextLine();
            if (IndicationType.types.contains(indicationType)) {
                return indicationType;
            } else {
                System.out.println("Некорректный тип показания");
            }
        }
    }

    /**
     * Print a user menu and get the next step of the user
     *
     * @return user step
     */
    private int getNextUserStep() {
        while (true) {
            System.out.println("Выберите действие: \n" +
                    "1 - Внести показания \n" +
                    "2 - Посмотреть последние введенные показания \n" +
                    "3 - Посмотреть показания за выбранный месяц \n" +
                    "4 - Посмотреть историю подачи показаний \n" +
                    "5 - Выйти из учетной записи\n" +
                    "6 - Завершить работу");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }
    }

    /**
     * Print available actions for admin and call methods of MonitoringService to do them.
     * If the user logs out of the account - returns the name of the new user.
     * If the user wants to terminate the application - returns null
     *
     * @return new username or null
     */
    private String printAdminActions() {
        while (true) {
            int step = getNextAdminStep();

            switch (step) {
                case 1:
                    System.out.println(monitoringService.getAllIndications());
                    break;
                case 2:
                    userAuditTrail.forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Введите тип показаний");
                    String newType = scanner.nextLine();
                    IndicationType.addType(newType);
                    break;
                case 4:
                    return getAuthenticationSteps();
                case 5:
                    return null;
                default:
                    System.out.println("Введите корректное число");
            }
        }
    }

    /**
     * Print an admin menu and get the next step of the admin
     *
     * @return admin step
     */
    private int getNextAdminStep() {
        while (true) {
            System.out.println("Выберите действие: \n" +
                    "1 - Посмотреть показания всех пользователей \n" +
                    "2 - Аудит действий пользователей \n" +
                    "3 - Добавление нового типа показаний \n" +
                    "4 - Выйти из учетной записи \n" +
                    "5 - Завершить работу");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }
    }

    /**
     * Print available actions (registration or authentication) and call methods of MonitoringService to do them.
     *
     * @return new username
     */
    private String getAuthenticationSteps() {
        int step;
        while (true) {
            try {
                System.out.println("Выберите действие: \n" +
                        "1 - Зарегистрироваться \n" +
                        "2 - Войти");
                step = Integer.parseInt(scanner.nextLine());
                if (step == 1 || step == 2) {
                    break;
                } else {
                    System.out.println("Введите корректное число");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }

        String name = logIn(step);
        if (name == null) {
            name = getAuthenticationSteps();
        }
        return name;
    }

    private String logIn(int step) {
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        return (step == 1) ? authService.registerUser(name, password) : authService.authUser(name, password);
    }
}
