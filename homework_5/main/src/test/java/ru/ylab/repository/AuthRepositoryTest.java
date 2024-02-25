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
import ru.ylab.domain.model.User;
import ru.ylab.repository.impl.AuthRepositoryImpl;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class AuthRepositoryTest {
    @InjectMocks
    private AuthRepositoryImpl authRepository;
    @Mock
    private DataSource dataSource;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"))
            .withExposedPorts(5432);

    @BeforeAll
    public static void setUp() {
        JDBCConfigTest.doMigration(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    }

    @AfterEach
    @SneakyThrows
    public void delete() {
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        authRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Добавление пользователя в таблицу")
    public void addUserTest() {
        String username = "nn";
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        authRepository.registerUser(username, "p");
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());

        User result = authRepository.getUser(username);

        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    @SneakyThrows
    @DisplayName("Определение существования пользователя")
    public void ifExistUserTest() {
        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        boolean res = authRepository.hasUser("n");
        assertThat(res).isFalse();

        when(dataSource.getConnection()).thenReturn(JDBCConfigTest.connect());
        boolean res2 = authRepository.hasUser("admin");
        assertThat(res2).isTrue();
    }
}
