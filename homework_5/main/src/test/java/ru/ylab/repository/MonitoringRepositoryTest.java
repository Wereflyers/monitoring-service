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
import ru.ylab.domain.model.Indication;
import ru.ylab.repository.impl.MonitoringRepositoryImpl;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class MonitoringRepositoryTest {
    @InjectMocks
    MonitoringRepositoryImpl monitoringRepository;
    @Mock
    private DataSource dataSource;
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
        indication1 = new Indication(1L, "ГВ", LocalDate.now(), 123L, "first_user");
        indication2 = new Indication(1L, "ХВ", LocalDate.of(2024, 1, 1),
                123L, "second_user");
    }

    @AfterEach
    @SneakyThrows
    public void delete() {
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Добавление показания в таблицу")
    public void addIndicationTest() {
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication1, 1L);

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        Indication result = monitoringRepository.getLastIndication(1L, "first_user");

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.getUsername()).describedAs("username")
                    .isEqualTo(indication1.getUsername());
            softAssertions.assertThat(result.getType()).describedAs("type")
                    .isEqualTo(indication1.getType());
            softAssertions.assertThat(result.getValue()).describedAs("value")
                    .isEqualTo(indication1.getValue());
            softAssertions.assertThat(result.getDate()).describedAs("date")
                    .isEqualTo(indication1.getDate());
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Просмотр показания за месяц")
    public void checkIndicationForMonthTest() {
        sendTwoIndications();

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        Indication result1 = monitoringRepository.checkIndicationForMonth("first_user", 1L, 2);

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        Indication result2 = monitoringRepository.checkIndicationForMonth("first_user", 1L, 3);

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        Indication result3 = monitoringRepository.checkIndicationForMonth("second_user", 2L, 1);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result1.getValue()).describedAs("first indication value")
                    .isEqualTo(indication1.getValue());
            softAssertions.assertThat(result2).describedAs("second indication").isNull();
            softAssertions.assertThat(result3.getValue()).describedAs("third indication value")
                    .isEqualTo(indication2.getValue());
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Выгрузка всех показаний")
    public void getAllIndicationsTest() {
        sendTwoIndications();

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        List<Indication> result = monitoringRepository.getAllIndications();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(2);
            softAssertions.assertThat(result.get(0).getType()).describedAs("first indication type")
                    .isEqualTo(indication1.getType());
            softAssertions.assertThat(result.get(0).getValue()).describedAs("first indication value")
                    .isEqualTo(indication1.getValue());
            softAssertions.assertThat(result.get(1).getType()).describedAs("second indication type")
                    .isEqualTo(indication2.getType());
            softAssertions.assertThat(result.get(1).getValue()).describedAs("second indication value")
                    .isEqualTo(indication2.getValue());
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Выгрузка всех показаний одного пользователя")
    public void getAllIndicationsOfUserTest() {
        sendTwoIndications();

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        List<Indication> result = monitoringRepository.getAllIndicationsOfUser("first_user");

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(1);
            softAssertions.assertThat(result.get(0).getType()).describedAs("type")
                    .isEqualTo(indication1.getType());
            softAssertions.assertThat(result.get(0).getValue()).describedAs("value")
                    .isEqualTo(indication1.getValue());
            softAssertions.assertThat(result.get(0).getUsername()).describedAs("username")
                    .isEqualTo(indication1.getUsername());
        });
    }

    @SneakyThrows
    private void sendTwoIndications() {
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication1, 1);
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        monitoringRepository.sendIndication(indication2, 2);
    }
}
