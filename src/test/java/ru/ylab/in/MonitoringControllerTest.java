package ru.ylab.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.model.IndicationType;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.time.LocalDate;
import java.util.List;
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
    private IndicationTypeService indicationTypeService;
    @Mock
    private Scanner scanner;

    @Test
    @DisplayName(value = "Получение пользователем последнего показания")
    void distributeRolesTest_whenPrintLastIndication() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("2").thenReturn("ГВ").thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(monitoringService.checkLastIndicationAmount(1L, "name")).thenReturn("123");
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication(1L, "name", LocalDate.now(), 123L);
        verify(monitoringService).checkLastIndicationAmount(1L, "name");
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
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication(1L, "name", LocalDate.now(), 123L);
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
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication(1L, "name", LocalDate.now(), 123L);
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
        when(indicationTypeService.getType("NEW")).thenReturn(new IndicationType(4L, "NEW"));
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).authUser("admin", "PASSWORD123");
        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication(4L, "name", LocalDate.now(), 123L);
    }

    @Test
    @DisplayName(value = "Получение пользователем показания за выбранный месяц")
    void distributeRolesTest_whenGetIndicationForMonth() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("3").thenReturn("1").thenReturn("ГВ")
                .thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(monitoringService.checkIndicationForMonth("name", 1L, 1)).thenReturn(123L);
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).checkIndicationForMonth("name", 1L, 1);
    }

    @Test
    @DisplayName(value = "Получение пользователем показания за выбранный месяц")
    void distributeRolesTest_wGetIndicationForMonth() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("password").thenReturn("1")
                .thenReturn("ГВ").thenReturn("123").thenReturn("3").thenReturn("2").thenReturn("ГВ")
                .thenReturn("6");

        when(authService.registerUser("name", "password")).thenReturn("name");
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(monitoringService.checkIndicationForMonth("name", 1L, 2)).thenReturn(123L);
        when(indicationTypeService.getAllTypes()).thenReturn(List.of("ГВ", "ХВ", "Отопление"));

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).checkIndicationForMonth("name", 1L, 2);
    }
}