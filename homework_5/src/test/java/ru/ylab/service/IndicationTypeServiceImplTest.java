package ru.ylab.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.repository.IndicationTypeRepository;
import ru.ylab.service.impl.IndicationTypeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndicationTypeServiceImplTest {
    @InjectMocks
    private IndicationTypeServiceImpl indicationTypeServiceImpl;
    @Mock
    private IndicationTypeRepository indicationTypeRepository;

    @Test
    @DisplayName(value = "Тест получение типа показания")
    @SneakyThrows
    void getTypeTest() {
        when(indicationTypeRepository.getTypeByName("type"))
                .thenReturn(new IndicationType(4L, "type"));

        IndicationType actual = indicationTypeServiceImpl.getType("type");

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

        List<String> actual = indicationTypeServiceImpl.getAllTypes();

        assertThat(actual).isEqualTo(indicationTypes);
    }

    @Test
    @DisplayName(value = "Тест добавления показания")
    @SneakyThrows
    void addTypeTest() {
        when(indicationTypeRepository.getTypeByName("type")).thenReturn(null);

        indicationTypeServiceImpl.addType("type");

        verify(indicationTypeRepository).addType("type");
    }

    @Test
    @DisplayName(value = "Тест добавления показания, когда такое название показания уже имеется")
    @SneakyThrows
    void addTypeTest_whenHaveSame() {
        when(indicationTypeRepository.getTypeByName("type"))
                .thenReturn(new IndicationType(4L, "type"));

        verify(indicationTypeRepository, never()).addType("type");
        assertThatThrownBy(() -> indicationTypeServiceImpl.addType("type"))
                .isInstanceOf(WrongDataException.class);
    }
}
