package ru.ylab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab.service.MonitoringService;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

class MonitoringServiceTest {
    private MonitoringService monitoringService;

    @BeforeEach
    public void setUp() {
        monitoringService = new MonitoringService();
    }

    @Test
    @DisplayName(value = "Отправка показания")
    void sendIndicationTest() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator()
                + "ХВ 2023-02-01 - 222" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ХВ", "name", LocalDate.of(2023, 2, 1), 222L);

        String actual = monitoringService.getAllIndications();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Отправка показания с некорректным значением")
    void sendIndicationTest_whenValueIsLessThenLast() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 2, 1), 111L);

        String actual = monitoringService.getAllIndications();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Отправка показания за тот же месяц")
    void sendIndicationTest_whenMonthIsSame() {
        String expected = "name" + System.lineSeparator()
                + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 21), 133L);

        String actual = monitoringService.getAllIndications();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение показания за выбранный месяц")
    void checkIndicationForMonthTest() {
        Long expected = 123L;

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), expected);
        Long actualForMonth1 = monitoringService.checkIndicationForMonth("name", "ГВ", 1);
        Long actualForMonth2 = monitoringService.checkIndicationForMonth("name", "ГВ", 2);

        assertThat(actualForMonth1)
                .isEqualTo(expected);
        assertThat(actualForMonth2)
                .isNull();
    }

    @Test
    @DisplayName(value = "Получение последнего показания")
    void checkLastIndicationAmountTest() {
        Long expected = 123L;

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), expected);
        String actual = monitoringService.checkLastIndicationAmount("ГВ", "name");

        assertThat(actual)
                .isEqualTo(expected.toString());
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя")
    void getAllIndicationsOfUserTest() {
        String expected = "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1), 123L);
        String actual = monitoringService.getAllIndicationsOfUser("name");

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя, если не было передано ни одного")
    void getAllIndicationsOfUserTest_whenEmpty() {
        String expected = "Нет введенных показаний";

        monitoringService.sendIndication("ГВ", "name", LocalDate.now(), 123L);

        String actual = monitoringService.getAllIndicationsOfUser("secondUser");

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение показаний всех пользователей")
    void getAllIndicationsTest() {
        String expected = "name" + System.lineSeparator() + "ГВ 2023-01-01 - 123" + System.lineSeparator();

        monitoringService.sendIndication("ГВ", "name", LocalDate.of(2023, 1, 1),
                123L);
        String actual = monitoringService.getAllIndications();

        assertThat(actual)
                .isEqualTo(expected);
    }
}