package ru.ylab.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
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
import ru.ylab.domain.model.Indication;
import ru.ylab.repository.impl.MonitoringRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class MonitoringRepositoryTest {
    @InjectMocks
    MonitoringRepositoryImpl monitoringRepository;
    @Mock
    private JDBCConfig config;
    private static Indication indication1;
    private static Indication indication2;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"))
            .withExposedPorts(5432);

    @BeforeAll
    @SneakyThrows
    public static void setUp() {
        JDBCConfigTest.doMigration(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        indication1 = new Indication("ГВ", LocalDate.now(), 123L, "first_user");
        indication2 = new Indication("ХВ", LocalDate.of(2024, 1, 1),
                123L, "second_user");
    }

    @AfterEach
    @SneakyThrows
    public void delete() {
        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Добавление показания в таблицу")
    public void addIndicationTest() {
        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication1, 1L);

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        Indication result = monitoringRepository.getLastIndication(1L, "first_user");

        assertThat(result.getUsername()).isEqualTo(indication1.getUsername());
        assertThat(result.getType()).isEqualTo(indication1.getType());
        assertThat(result.getValue()).isEqualTo(indication1.getValue());
        assertThat(result.getDate()).isEqualTo(indication1.getDate());
    }

    @Test
    @SneakyThrows
    @DisplayName("Просмотр показания за месяц")
    public void checkIndicationForMonthTest() {
        sendTwoIndications();

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        Indication result1 = monitoringRepository.checkIndicationForMonth("first_user", 1L, 2);

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        Indication result2 = monitoringRepository.checkIndicationForMonth("first_user", 1L, 3);

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        Indication result3 = monitoringRepository.checkIndicationForMonth("second_user", 2L, 1);

        assertThat(result1.getValue()).isEqualTo(indication1.getValue());
        assertThat(result2).isNull();
        assertThat(result3.getValue()).isEqualTo(indication2.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName("Выгрузка всех показаний")
    public void getAllIndicationsTest() {
        sendTwoIndications();

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        List<Indication> result = monitoringRepository.getAllIndications();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getType()).isEqualTo(indication1.getType());
        assertThat(result.get(0).getValue()).isEqualTo(indication1.getValue());
        assertThat(result.get(1).getType()).isEqualTo(indication2.getType());
        assertThat(result.get(1).getValue()).isEqualTo(indication2.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName("Выгрузка всех показаний одного пользователя")
    public void getAllIndicationsOfUserTest() {
        sendTwoIndications();

        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        List<Indication> result = monitoringRepository.getAllIndicationsOfUser("first_user");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getType()).isEqualTo(indication1.getType());
        assertThat(result.get(0).getValue()).isEqualTo(indication1.getValue());
        assertThat(result.get(0).getUsername()).isEqualTo(indication1.getUsername());
    }

    @SneakyThrows
    private void sendTwoIndications() {
        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication1, 1);
        when(config.connect()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication2, 2);
    }
}
