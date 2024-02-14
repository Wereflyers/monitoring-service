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
import ru.ylab.dto.IndicationDto;
import ru.ylab.model.Indication;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllIndicationOfUserServletTest {
    @InjectMocks
    private GetAllIndicationsOfUserServlet servlet;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MonitoringService monitoringService;
    @Mock
    private AuthService authService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter out;
    @Captor
    private ArgumentCaptor<List<IndicationDto>> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний")
    void doGetTest() {
        String username = "name";
        Indication indication = new Indication("ГВ", LocalDate.now(), 123L, username);

        when(request.getHeader("username")).thenReturn(username);
        when(monitoringService.getAllIndicationsOfUser(username)).thenReturn(List.of(indication));
        when(authService.hasUser(username)).thenReturn(true);
        when(objectMapper.writeValueAsString(any())).thenReturn("string");
        when(response.getWriter()).thenReturn(out);

        servlet.doGet(request, response);

        verify(objectMapper).writeValueAsString(requestArgumentCaptor.capture());
        List<IndicationDto> result = requestArgumentCaptor.getValue();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getType()).isEqualTo(indication.getType());
        assertThat(result.get(0).getValue()).isEqualTo(indication.getValue());
    }
}