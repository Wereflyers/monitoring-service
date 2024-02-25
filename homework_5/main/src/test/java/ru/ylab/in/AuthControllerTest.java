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
import ru.ylab.domain.dto.UserDto;
import ru.ylab.exceptions.ErrorHandler;
import ru.ylab.mapper.UserMapper;
import ru.ylab.service.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainWebAppInitializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthServiceImpl authServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(
                        authServiceImpl,
                        userMapper))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест регистрации пользователя")
    void registerTest() {
        UserDto userDto = new UserDto("username", "PassPass1");

        when(authServiceImpl.registerUser(any())).thenReturn(userDto.getUsername());

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertThat(result).isEqualTo("Registration is successful. Welcome, username");
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест авторизации пользователя")
    void loginTest() {
        UserDto userDto = new UserDto("username", "password");

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertThat(result).isEqualTo("Login successful. Welcome, username");
    }
}
