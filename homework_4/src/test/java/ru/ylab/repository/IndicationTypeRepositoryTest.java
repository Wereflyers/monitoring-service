package ru.ylab.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.ylab.JDBCConfigTest;
import ru.ylab.config.JDBCConfig;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.repository.impl.IndicationTypeRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class IndicationTypeRepositoryTest {
    @InjectMocks
    private IndicationTypeRepositoryImpl indicationTypeRepository;
    @Mock
    private JDBCConfig config;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"))
            .withExposedPorts(5432);

    @BeforeAll
    public static void setUp() {
        JDBCConfigTest.doMigration(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @Test
    @SneakyThrows
    @DisplayName("Добавление типа в таблицу")
    public void addTypeTest() {
        String type = "New_type";

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        indicationTypeRepository.addType(type);
        when(config.connect()).thenReturn(JDBCConfigTest.connect());

        IndicationType result = indicationTypeRepository.getTypeByName(type);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(type.toUpperCase());

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        indicationTypeRepository.delete(type);
    }

    @Test
    @SneakyThrows
    @DisplayName("Выгрузка всех типов показаний")
    public void getAllTypes() {
        List<String> types = List.of("ГВ", "ХВ", "ОТОПЛЕНИЕ");
        when(config.connect()).thenReturn(JDBCConfigTest.connect());

        List<String> result = indicationTypeRepository.getAllTypes();

        assertThat(result).isEqualTo(types);
    }
}
