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

import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddIndicationServletTest {
    @InjectMocks
    private AddIndicationServlet addIndicationServlet;
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
    private ArgumentCaptor<Indication> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления показания")
    void doPostTest() {
        IndicationDto indicationDto = new IndicationDto("ГВ", 123L);
        String username = "name";

        when(request.getInputStream()).thenReturn(null);
        when(request.getHeader("username")).thenReturn(username);
        when(authService.isUserExist(username)).thenReturn(true);
        when(objectMapper.readValue((InputStream) null, IndicationDto.class)).thenReturn(indicationDto);
        when(response.getWriter()).thenReturn(out);

        addIndicationServlet.doPost(request, response);

        verify(monitoringService).sendIndication(requestArgumentCaptor.capture());
        Indication result = requestArgumentCaptor.getValue();

        assertThat(result.getType()).isEqualTo(indicationDto.getType());
        assertThat(result.getValue()).isEqualTo(indicationDto.getValue());
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }
}