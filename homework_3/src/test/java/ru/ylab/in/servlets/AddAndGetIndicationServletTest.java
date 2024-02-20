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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddAndGetIndicationServletTest {
    @InjectMocks
    private AddAndGetIndicationServlet servlet;
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
    @Captor
    private ArgumentCaptor<List<IndicationDto>> requestDtoArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления показания")
    void doPostTest() {
        IndicationDto indicationDto = new IndicationDto("ГВ", 123L);
        String username = "name";

        when(request.getInputStream()).thenReturn(null);
        when(request.getHeader("username")).thenReturn(username);
        when(authService.hasUser(username)).thenReturn(true);
        when(objectMapper.readValue((InputStream) null, IndicationDto.class)).thenReturn(indicationDto);
        when(response.getWriter()).thenReturn(out);

        servlet.doPost(request, response);

        verify(monitoringService).sendIndication(requestArgumentCaptor.capture());
        Indication result = requestArgumentCaptor.getValue();

        assertThat(result.getType()).isEqualTo(indicationDto.getType());
        assertThat(result.getValue()).isEqualTo(indicationDto.getValue());
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех показаний")
    void doGetTest() {
        String username = "name";
        Indication indication = new Indication("ГВ", LocalDate.now(), 123L, username);

        when(monitoringService.getAllIndications()).thenReturn(List.of(indication));
        when(request.getHeader("username")).thenReturn("admin");
        when(authService.hasUser("admin")).thenReturn(true);
        when(objectMapper.writeValueAsString(any())).thenReturn("string");
        when(response.getWriter()).thenReturn(out);

        servlet.doGet(request, response);

        verify(objectMapper).writeValueAsString(requestDtoArgumentCaptor.capture());
        List<IndicationDto> result = requestDtoArgumentCaptor.getValue();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getType()).isEqualTo(indication.getType());
        assertThat(result.get(0).getValue()).isEqualTo(indication.getValue());
    }
}