package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.dto.UserDto;
import ru.ylab.model.User;
import ru.ylab.service.AuthService;

import java.io.InputStream;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @InjectMocks
    private LoginServlet loginServlet;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AuthService authService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter out;
    @Mock
    private HttpSession session;
    @Captor
    private ArgumentCaptor<User> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода авторизации")
    void doPostTest() {
        UserDto userDto = new UserDto("name", "pass");

        when(request.getInputStream()).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(objectMapper.readValue((InputStream) null, UserDto.class)).thenReturn(userDto);
        when(response.getWriter()).thenReturn(out);

        loginServlet.doPost(request, response);

        verify(authService).authUser(requestArgumentCaptor.capture());
        User result = requestArgumentCaptor.getValue();

        assertThat(result.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
    }
}