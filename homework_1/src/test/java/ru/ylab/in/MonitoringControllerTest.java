package ru.ylab.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.time.LocalDate;
import java.util.Scanner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {
    @InjectMocks
    private MonitoringController monitoringController;
    @Mock
    private MonitoringService monitoringService;
    @Mock
    private AuthService authService;
    @Mock
    private Scanner scanner;

    @Test
    @DisplayName(value = "Получение пользователем последнего показания")
    void distributeRolesTest_whenPrintLastIndication() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("2").thenReturn("ГВ").thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(monitoringService.checkLastIndicationAmount("ГВ", "name")).thenReturn("123");

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(monitoringService).checkLastIndicationAmount("ГВ", "name");
    }

    @Test
    @DisplayName(value = "Получение пользователем всех показаний")
    void distributeRolesTest_whenPrintAllIndications() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("5").thenReturn("2").thenReturn("admin")
                .thenReturn("PASSWORD123").thenReturn("1").thenReturn("5");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(authService.authUser("admin", "PASSWORD123")).thenReturn("admin");
        when(monitoringService.getAllIndications()).thenReturn("ГВ 2023-01-28 - 123");

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(authService).authUser("admin", "PASSWORD123");
        verify(monitoringService).getAllIndications();
    }

    @Test
    @DisplayName(value = "Получение админом аудита действий пользователей")
    void distributeRolesTest_whenGetAuditTrail() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("5").thenReturn("2").thenReturn("admin")
                .thenReturn("PASSWORD123").thenReturn("2").thenReturn("5");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(authService.authUser("admin", "PASSWORD123")).thenReturn("admin");

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(authService).authUser("admin", "PASSWORD123");
    }

    @Test
    @DisplayName(value = "Добавление админом типа показаний")
    void distributeRolesTest_whenAddTypeOfIndication() {
        when(scanner.nextLine()).thenReturn("2").thenReturn("admin").thenReturn("PASSWORD123")
                .thenReturn("3").thenReturn("NEW").thenReturn("4").thenReturn("1").thenReturn("name")
                .thenReturn("password").thenReturn("1").thenReturn("NEW").thenReturn("123").thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(authService.authUser("admin", "PASSWORD123")).thenReturn("admin");

        monitoringController.distributeRoles();

        verify(authService).authUser("admin", "PASSWORD123");
        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("NEW", "name", LocalDate.now(), 123L);
    }

    @Test
    @DisplayName(value = "Получение пользователем показания за выбранный месяц")
    void distributeRolesTest_whenGetIndicationForMonth() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("3").thenReturn("1").thenReturn("ГВ")
                .thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(monitoringService.checkIndicationForMonth("name", "ГВ", 1)).thenReturn(123L);

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).checkIndicationForMonth("name", "ГВ", 1);
    }

    @Test
    @DisplayName(value = "Получение пользователем показания за выбранный месяц")
    void distributeRolesTest_wGetIndicationForMonth() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("5")
                .thenReturn("2").thenReturn("123").thenReturn("3").thenReturn("2").thenReturn("name")
                .thenReturn("password").thenReturn("6");



        when(authService.registerUser("name", "password")).thenReturn("name");
        when(authService.authUser("name", "password")).thenReturn("name");
        when(authService.authUser("123", "3")).thenReturn(null);

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).checkIndicationForMonth("name", "ГВ", 1);
    }
}