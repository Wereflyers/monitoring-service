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
import ru.ylab.domain.dto.IndicationTypeDto;
import ru.ylab.exceptions.ErrorHandler;
import ru.ylab.service.impl.IndicationTypeServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainWebAppInitializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IndicationTypeControllerTest {
    private MockMvc mockMvc;
    @Mock
    private IndicationTypeServiceImpl indicationTypeServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new IndicationTypeController(
                        indicationTypeServiceImpl))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест добавления типа показания")
    void addTypeTest() {
        when(indicationTypeServiceImpl.getAllTypes()).thenReturn(List.of("Hot", "Cold", "Firing"));

        MvcResult mvcResult = mockMvc.perform(post("/indication/type")
                        .header("username", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IndicationTypeDto("Firing"))))
                .andExpect(status().isCreated())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertThat(result).isEqualTo("[\"Hot\",\"Cold\",\"Firing\"]");
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест получения всех типов показаний")
    void addAllTypesTest() {
        when(indicationTypeServiceImpl.getAllTypes()).thenReturn(List.of("Hot", "Cold", "Firing"));

        MvcResult mvcResult = mockMvc.perform(get("/indication/type")
                        .header("username", "admin"))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertThat(result).isEqualTo("[\"Hot\",\"Cold\",\"Firing\"]");
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления типа показания пользователем, не являющимся администратором")
    void addIndicationTypeTest_whenNoRights() {
        mockMvc.perform(post("/indication/type")
                        .header("username", "some_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IndicationTypeDto("Новый тип"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(
                        "Вы имеете недостаточно прав для выполнения данной операции"));
    }
}
