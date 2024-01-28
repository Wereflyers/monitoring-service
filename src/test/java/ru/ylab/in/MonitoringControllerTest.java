package ru.ylab.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {
    private MonitoringController monitoringController;
    @Mock
    private MonitoringService monitoringService;
    @Mock
    private AuthService authService;

    @Test
    void distributeRolesTest_whenPrintLastIndication() {
        String input = "1\nname\npassword\n1\nГВ\n123\n2\nГВ\n6";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        monitoringController = new MonitoringController(in, monitoringService, authService);

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(monitoringService).checkLastIndicationAmount("ГВ", "name");
    }

    @Test
    void distributeRolesTest_whenPrintAllIndications() {
        String input = "1\nname\npassword\n1\nГВ\n123\n5\n2\nadmin\nPASSWORD123\n1\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        monitoringController = new MonitoringController(in, monitoringService, authService);
        when(monitoringService.getAllIndications()).thenReturn("ГВ 2023-01-28 - 123");

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(authService).authUser("admin", "PASSWORD123");
        verify(monitoringService).getAllIndications();
    }

    @Test
    void distributeRolesTest_whenGetAuditTrail() {
        String input = "1\nname\npassword\n1\nГВ\n123\n5\n2\nadmin\nPASSWORD123\n2\n5";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        monitoringController = new MonitoringController(in, monitoringService, authService);

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("ГВ", "name", LocalDate.now(), 123L);
        verify(authService).authUser("admin", "PASSWORD123");
    }

    @Test
    void distributeRolesTest_whenAddTypeOfIndication() {
        String input = "2\nadmin\nPASSWORD123\n3\nNEW\n4\n1\nname\npassword\n1\nNEW\n123\n6";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        monitoringController = new MonitoringController(in, monitoringService, authService);

        monitoringController.distributeRoles();

        verify(authService).authUser("admin", "PASSWORD123");
        verify(authService).registerUser("name", "password");
        verify(monitoringService).sendIndication("NEW", "name", LocalDate.now(), 123L);
    }

    @Test
    void distributeRolesTest_whenGetIndicationForMonth() {
        String input = "1\nname\npassword\n1\nГВ\n123\n3\n1\n6";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        monitoringController = new MonitoringController(in, monitoringService, authService);

        monitoringController.distributeRoles();

        verify(authService).registerUser("name", "password");
        verify(monitoringService).checkIndicationForMonth("name", 1);
    }
}