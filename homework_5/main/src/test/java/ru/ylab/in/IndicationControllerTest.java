package ru.ylab.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.config.MainWebAppInitializer;
import ru.ylab.domain.dto.IndicationDto;
import ru.ylab.domain.model.Indication;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.exceptions.ErrorHandler;
import ru.ylab.mapper.IndicationMapper;
import ru.ylab.service.impl.AuthServiceImpl;
import ru.ylab.service.impl.IndicationTypeServiceImpl;
import ru.ylab.service.impl.MonitoringServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainWebAppInitializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IndicationControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MonitoringServiceImpl monitoringServiceImpl;
    @Mock
    private AuthServiceImpl authServiceImpl;
    @Mock
    private IndicationTypeServiceImpl indicationTypeServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IndicationMapper indicationMapper;
    private Indication indication;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new IndicationController(
                        indicationMapper,
                        monitoringServiceImpl, authServiceImpl,
                        indicationTypeServiceImpl))
                .setControllerAdvice(new ErrorHandler())
                .build();
        indication = new Indication(1L, "HB", LocalDate.now(), 123L, "name");
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления показания")
    void addIndicationTest() {
        IndicationDto indicationDto = new IndicationDto("HB", 123L);
        String username = "name";

        when(authServiceImpl.hasUser(username)).thenReturn(true);
        when(monitoringServiceImpl.sendIndication(any())).thenReturn(
                indicationMapper.toIndication(indicationDto, username, LocalDate.now()));

        MvcResult mvcResult = mockMvc.perform(post("/indication")
                        .header("username", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(indicationDto)))
                .andExpect(status().isCreated())
                .andReturn();

        IndicationDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                IndicationDto.class);

        assertThat(result.getType()).as("indication type").isEqualTo(indicationDto.getType());
        assertThat(result.getValue()).as("indication value").isEqualTo(indicationDto.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления показания незарегистрированным пользователем")
    void addIndicationTest_whenNoRights() {
        when(authServiceImpl.hasUser("some_name")).thenReturn(false);

        mockMvc.perform(post("/indication")
                        .header("username", "some_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IndicationDto("name", 123L))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        "Вы имеете недостаточно прав для выполнения данной операции"));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний")
    void getAllIndicationTest() {
        Indication indication = new Indication(1L, "HB", LocalDate.now(), 123L, "name");

        when(monitoringServiceImpl.getAllIndications()).thenReturn(List.of(indication));

        mockMvc.perform(get("/indication")
                        .header("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value(indication.getType()))
                .andExpect(jsonPath("$[0].value").value(indication.getValue()));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний пользователем, не являющимся администратором")
    void getAllIndicationTest_whenNoRights() {
        mockMvc.perform(get("/indication")
                        .header("username", "some_name"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        "Вы имеете недостаточно прав для выполнения данной операции"));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний пользователя")
    void getAllIndicationsOfUser() {
        String username = "name";

        when(monitoringServiceImpl.getAllIndicationsOfUser(username)).thenReturn(List.of(indication));
        when(authServiceImpl.hasUser(username)).thenReturn(true);

        mockMvc.perform(get("/indication/all")
                        .header("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value(indication.getType()))
                .andExpect(jsonPath("$[0].value").value(indication.getValue()));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний незарегистрированным пользователем")
    void getAllIndicationOfUserTest_whenNoRights() {
        when(authServiceImpl.hasUser("some_name")).thenReturn(false);

        mockMvc.perform(get("/indication/all")
                        .header("username", "some_name"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        "Вы имеете недостаточно прав для выполнения данной операции"));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения последнего показания")
    void getLastIndicationTest() {
        String username = "name";
        String type = "HB";

        when(monitoringServiceImpl.getLastIndication(1L, username)).thenReturn(indication);
        when(authServiceImpl.hasUser(username)).thenReturn(true);
        when(indicationTypeServiceImpl.getType(type)).thenReturn(new IndicationType(1L, type));

        MvcResult mvcResult = mockMvc.perform(get("/indication/last")
                        .param("type", type)
                        .header("username", username))
                .andExpect(status().isOk())
                .andReturn();

        IndicationDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                IndicationDto.class);

        assertThat(result.getType()).isEqualTo(indication.getType());
        assertThat(result.getValue()).isEqualTo(indication.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения последнего показания незарегистрированным пользователем")
    void getLastIndicationTest_whenNoRights() {
        when(authServiceImpl.hasUser("some_name")).thenReturn(false);

        mockMvc.perform(get("/indication/last")
                        .header("username", "some_name")
                        .param("type", "type"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        "Вы имеете недостаточно прав для выполнения данной операции"));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения показания за месяц")
    void getIndicationForMonthTest() {
        String username = "name";
        String type = "HB";

        when(monitoringServiceImpl.getIndicationForMonth(1L, username, 2)).thenReturn(indication);
        when(authServiceImpl.hasUser(username)).thenReturn(true);
        when(indicationTypeServiceImpl.getType(type)).thenReturn(new IndicationType(1L, type));

        MvcResult mvcResult = mockMvc.perform(get("/indication/last")
                        .param("type", type)
                        .param("month", "2")
                        .header("username", username))
                .andExpect(status().isOk())
                .andReturn();

        IndicationDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                IndicationDto.class);

        assertThat(result.getType()).as("indication type").isEqualTo(indication.getType());
        assertThat(result.getValue()).as("indication value").isEqualTo(indication.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения показания за некорректный месяц")
    void getIndicationForMonthTest_whenWrongMonth() {
        when(authServiceImpl.hasUser("some_name")).thenReturn(true);
        when(indicationTypeServiceImpl.getType("type")).thenReturn(new IndicationType(1L, "type"));

        mockMvc.perform(get("/indication/last")
                        .header("username", "some_name")
                        .param("month", "111")
                        .param("type", "type"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(
                        "Введите число месяца"));
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения показания некорректного типа")
    void getIndicationForMonthTest_whenWrongType() {
        when(authServiceImpl.hasUser("some_name")).thenReturn(true);
        when(indicationTypeServiceImpl.getType("type")).thenReturn(null);

        mockMvc.perform(get("/indication/last")
                        .header("username", "some_name")
                        .param("month", "111")
                        .param("type", "type"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(
                        "Некорректный тип показания"));
    }
}
