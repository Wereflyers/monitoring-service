package ru.ylab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.service.MonitoringService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MonitoringServiceTest {
    private MonitoringService monitoringService;

    @BeforeEach
    public void setUp() {
        monitoringService = new MonitoringService();
    }

    @Test
    void sendIndicationTest() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator()
                + "ХВ 2023-02-01 - 222" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ХВ", "name", LocalDate.of(2023, 2, 1), 222L);

        String actual = monitoringService.getAllIndications();

        assertEquals(expected, actual);
    }

    @Test
    void sendIndicationTest_whenValueIsLessThenLast() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 2, 1), 111L);

        String actual = monitoringService.getAllIndications();

        assertEquals(expected, actual);
    }

    @Test
    void sendIndicationTest_whenMonthIsSame() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 21), 133L);

        String actual = monitoringService.getAllIndications();

        assertEquals(expected, actual);
    }

    @Test
    void checkIndicationForMonthTest() {
        Long expected = 123L;

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), expected);
        Long actualForMonth1 = monitoringService.checkIndicationForMonth("name", 1);
        Long actualForMonth2 = monitoringService.checkIndicationForMonth("name", 2);

        assertEquals(expected, actualForMonth1);
        assertNotEquals(expected, actualForMonth2);
    }

    @Test
    void checkLastIndicationAmountTest() {
        Long expected = 123L;

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), expected);
        String actual = monitoringService.checkLastIndicationAmount("ГВ", "name");

        assertEquals(expected.toString(), actual);
    }

    @Test
    void getAllIndicationsOfUserTest() {
        String expected = "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        String actual = monitoringService.getAllIndicationsOfUser("name");

        assertEquals(expected, actual);
    }

    @Test
    void getAllIndicationsOfUserTest_whenEmpty() {
        String expected = "Нет введенных показаний";

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), 123L);
        String actual = monitoringService.getAllIndicationsOfUser("secondUser");

        assertEquals(expected, actual);
    }

    @Test
    void getAllIndicationsTest() {
        String expected = "name" + System.lineSeparator() + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1),
                123L);
        String actual = monitoringService.getAllIndications();

        assertEquals(expected, actual);
    }
}