package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.IndicationType;
import ru.ylab.repository.IndicationTypeRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndicationTypeServiceTest {
    @InjectMocks
    private IndicationTypeService indicationTypeService;
    @Mock
    private IndicationTypeRepository indicationTypeRepository;

    @Test
    @DisplayName(value = "Тест получение типа показания")
    @SneakyThrows
    void getTypeTest() {
        when(indicationTypeRepository.getTypeByName("type"))
                .thenReturn(new IndicationType(4L, "type"));

        IndicationType actual = indicationTypeService.getType("type");

        assertThat(actual.getId()).isEqualTo(4L);
        assertThat(actual.getName()).isEqualTo("type");
    }

    @Test
    @DisplayName(value = "Тест получения всех типов показаний")
    @SneakyThrows
    void getAllTypesTest() {
        List<String> indicationTypes = new ArrayList<>();
        indicationTypes.add("ГВ");
        indicationTypes.add("ХВ");
        indicationTypes.add("ОТОПЛЕНИЕ");

        when(indicationTypeRepository.getAllTypes()).thenReturn(indicationTypes);

        List<String> actual = indicationTypeService.getAllTypes();

        assertThat(actual).isEqualTo(indicationTypes);
    }

    @Test
    @DisplayName(value = "Тест добавления показания")
    @SneakyThrows
    void addTypeTest() {
        when(indicationTypeRepository.getTypeByName("type")).thenReturn(null);

        indicationTypeService.addType("type");

        verify(indicationTypeRepository).addType("type");
    }

    @Test
    @DisplayName(value = "Тест добавления показания, когда такое название показания уже имеется")
    @SneakyThrows
    void addTypeTest_whenHaveSame() {
        when(indicationTypeRepository.getTypeByName("type"))
                .thenReturn(new IndicationType(4L, "type"));

        verify(indicationTypeRepository, never()).addType("type");
        assertThatThrownBy(() -> indicationTypeService.addType("type"))
                .isInstanceOf(WrongDataException.class);
    }
}
