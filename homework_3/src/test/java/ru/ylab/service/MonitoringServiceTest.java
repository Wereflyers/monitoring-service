package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.Indication;
import ru.ylab.model.IndicationType;
import ru.ylab.repository.MonitoringRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringServiceTest {
    @InjectMocks
    private MonitoringService monitoringService;
    @Mock
    private MonitoringRepository monitoringRepository;
    @Mock
    private IndicationTypeService indicationTypeService;

    @Test
    @DisplayName(value = "Отправка показания")
    @SneakyThrows
    void sendIndicationTest() {
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(monitoringRepository.getLastIndication(1L, "name")).thenReturn(null);

        monitoringService.sendIndication(new Indication("ГВ", LocalDate.now(), 123L, "name"));

        verify(monitoringRepository).sendIndication(any(), anyLong());
    }

    @Test
    @DisplayName(value = "Отправка показания, когда за данный месяц уже отправляли показания")
    @SneakyThrows
    void sendIndicationTest_whenHaveOne() {
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now(), 123L));

        verify(monitoringRepository, never()).sendIndication(any(), anyLong());
        assertThatThrownBy(() -> monitoringService.sendIndication(new Indication("ГВ",
                LocalDate.now(), 133L, "name")))
                .isInstanceOf(WrongDataException.class);
    }

    @Test
    @DisplayName(value = "Отправка показания с некорректным значением")
    @SneakyThrows
    void sendIndicationTest_whenValueIsLessThanLast() {
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now().minusMonths(1), 123L));

        verify(monitoringRepository, never()).sendIndication(any(), anyLong());
        assertThatThrownBy(() -> monitoringService.sendIndication(new Indication("ГВ",
                LocalDate.now(), 103L, "name")))
                .isInstanceOf(WrongDataException.class);
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
    void checkLastIndicationTest() {
        long expected = 123L;

        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(new Indication(LocalDate.now(), 123L));

        Indication actual = monitoringService.getLastIndication(1L, "name");

        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @Test
    @DisplayName(value = "Получение последнего показания, когда не было передано ни одного показания данного типа")
    @SneakyThrows
    void checkLastIndicationAmountTest_whenHaveNoIndication() {
        when(monitoringRepository.getLastIndication(1L, "name"))
                .thenReturn(null);

        Indication actual = monitoringService.getLastIndication(1L, "name");

        assertThat(actual).isNull();
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя")
    @SneakyThrows
    void getAllIndicationsOfUserTest() {
        Indication expected = new Indication("ГВ", LocalDate.of(2023, 1, 1), 123L, "name");

        List<Indication> indications = new ArrayList<>();
        indications.add(expected);
        when(monitoringRepository.getAllIndicationsOfUser("name")).thenReturn(indications);

        List<Indication> actual = monitoringService.getAllIndicationsOfUser("name");

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getType()).isEqualTo(expected.getType());
        assertThat(actual.get(0).getValue()).isEqualTo(expected.getValue());
        assertThat(actual.get(0).getUsername()).isEqualTo(expected.getUsername());
    }

    @Test
    @DisplayName(value = "Получение всех показаний пользователя, если не было передано ни одного")
    @SneakyThrows
    void getAllIndicationsOfUserTest_whenEmpty() {
        when(monitoringRepository.getAllIndicationsOfUser("name")).thenReturn(null);

        List<Indication> actual = monitoringService.getAllIndicationsOfUser("name");

        assertThat(actual).isNull();
    }

    @Test
    @DisplayName(value = "Получение показаний всех пользователей")
    @SneakyThrows
    void getAllIndicationsTest() {
        Indication expected = new Indication("ГВ", LocalDate.of(2023, 1, 1), 123L, "name");

        List<Indication> indications = new ArrayList<>();
        indications.add(expected);
        when(monitoringRepository.getAllIndications()).thenReturn(indications);

        List<Indication> actual = monitoringService.getAllIndications();

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getType()).isEqualTo(expected.getType());
        assertThat(actual.get(0).getValue()).isEqualTo(expected.getValue());
        assertThat(actual.get(0).getUsername()).isEqualTo(expected.getUsername());
    }
}
