package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.IndicationType;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetIndicationForMonthServletTest {
    @InjectMocks
    private GetLastIndicationForMonthServlet servlet;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MonitoringService monitoringService;
    @Mock
    private IndicationTypeService indicationTypeService;
    @Mock
    private AuthService authService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter out;
    @Captor
    private ArgumentCaptor<String> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения последнего показания")
    void doGetTest() {
        String username = "name";

        when(request.getParameter("type")).thenReturn("ГВ");
        when(request.getParameter("month")).thenReturn("1");
        when(request.getHeader("username")).thenReturn(username);
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(authService.hasUser(username)).thenReturn(true);
        when(monitoringService.checkIndicationForMonth(username, 1L, 1)).thenReturn(123L);
        when(objectMapper.writeValueAsString(123L)).thenReturn("123");
        when(response.getWriter()).thenReturn(out);

        servlet.doGet(request, response);

        verify(out).write(requestArgumentCaptor.capture());
        String result = requestArgumentCaptor.getValue();

        assertThat(result).isEqualTo("123");
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения показания за месяц для неверного типа показания")
    void doGetTest_whenWrongType() {
        String username = "name";

        when(request.getParameter("type")).thenReturn("bb");
        when(request.getParameter("month")).thenReturn("1");
        when(request.getHeader("username")).thenReturn(username);
        when(authService.hasUser(username)).thenReturn(true);
        when(indicationTypeService.getType("bb")).thenReturn(null);

        assertThatThrownBy(() -> servlet.doGet(request, response))
                .isInstanceOf(WrongDataException.class);
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения показания за месяц для неверного номера месяца")
   void doGetTest_whenWrongMonth() {
        when(request.getParameter("month")).thenReturn("13");
        when(request.getHeader("username")).thenReturn("name");
        when(authService.hasUser("name")).thenReturn(true);

        assertThatThrownBy(() -> servlet.doGet(request, response))
                .isInstanceOf(WrongDataException.class);
    }
}