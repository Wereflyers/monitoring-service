package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.model.Indication;
import ru.ylab.repository.MonitoringRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringServiceTest {
    @InjectMocks
    private MonitoringService monitoringService;
    @Mock
    private MonitoringRepository monitoringRepository;

    @Test
    @DisplayName(value = "Отправка показания")
    @SneakyThrows
    void sendIndicationTest() {
        when(monitoringRepository.getLastIndication(1L, "name")).thenReturn(null);

        monitoringService.sendIndication(1L, "name", LocalDate.now(), 123L);

        verify(monitoringRepository).sendIndication(anyString(), any(), anyLong());
    }

    @Test
    @DisplayName(value = "Отправка показания, когда за данный месяц уже отправляли показания")
    @SneakyThrows
    void sendIndicationTest_whenHaveOne() {
        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now(), 123L));

        monitoringService.sendIndication(1L, "name", LocalDate.now(), 133L);

        verify(monitoringRepository, never()).sendIndication(anyString(), any(), anyLong());
    }

    @Test
    @DisplayName(value = "Отправка показания с некорректным значением")
    @SneakyThrows
    void sendIndicationTest_whenValueIsLessThenLast() {
        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now().minusMonths(1), 123L));

        monitoringService.sendIndication(1L, "name", LocalDate.now(), 103L);

        verify(monitoringRepository, never()).sendIndication(anyString(), any(), anyLong());
    }

    @Test
    @DisplayName(value = "Получение показания за выбранный месяц")
    @SneakyThrows
    void checkIndicationForMonthTest() {
        Long expected = 123L;

        when(monitoringRepository.checkIndicationForMonth("name", 1L, 2)).thenReturn(123L);

        Long actual = monitoringService.checkIndicationForMonth("name", 1L, 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение последнего показания")
    @SneakyThrows
    void checkLastIndicationAmountTest() {
        String expected = "123";

        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now(), 123L));

        String actual = monitoringService.checkLastIndicationAmount(1L, "name");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение последнего показания, когда не было передано ни одного показания данного типа")
    @SneakyThrows
    void checkLastIndicationAmountTest_whenHaveNoIndication() {
        String expected = "Нет введенных показаний";

        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(null);

        String actual = monitoringService.checkLastIndicationAmount(1L, "name");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя")
    @SneakyThrows
    void getAllIndicationsOfUserTest() {
        Indication expected = new Indication("ГВ", LocalDate.of(2023, 1, 1), 123L, "name");

        List<Indication> indications = new ArrayList<>();
        indications.add(expected);
        when(monitoringRepository.getAllIndicationsOfUser("name")).thenReturn(indications);

        String actual = monitoringService.getAllIndicationsOfUser("name");

        assertThat(actual).isEqualTo(expected + System.lineSeparator());
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя, если не было передано ни одного")
    @SneakyThrows
    void getAllIndicationsOfUserTest_whenEmpty() {
        String expected = "Нет введенных показаний";

        when(monitoringRepository.getAllIndicationsOfUser("name")).thenReturn(null);

        String actual = monitoringService.getAllIndicationsOfUser("name");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение показаний всех пользователей")
    @SneakyThrows
    void getAllIndicationsTest() {
        Indication expected = new Indication("ГВ", LocalDate.of(2023, 1, 1), 123L, "name");

        List<Indication> indications = new ArrayList<>();
        indications.add(expected);
        when(monitoringRepository.getAllIndications()).thenReturn(indications);

        String actual = monitoringService.getAllIndications();

        assertThat(actual).isEqualTo(expected + System.lineSeparator());
    }
}