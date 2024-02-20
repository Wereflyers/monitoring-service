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
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.Indication;
import ru.ylab.model.IndicationType;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.io.PrintWriter;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLastIndicationServletTest {
    @InjectMocks
    private GetLastIndicationServlet getLastIndicationServlet;
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
    private ArgumentCaptor<IndicationDto> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения последнего показания")
    void doGetTest() {
        String username = "name";
        Indication indication = new Indication("ГВ", LocalDate.now(), 123L, username);

        when(request.getParameter("type")).thenReturn("ГВ");
        when(request.getHeader("username")).thenReturn(username);
        when(indicationTypeService.getType("ГВ")).thenReturn(new IndicationType(1L, "ГВ"));
        when(authService.isUserExist(username)).thenReturn(true);
        when(monitoringService.getLastIndication(1L, username)).thenReturn(indication);
        when(objectMapper.writeValueAsString(any())).thenReturn("string");
        when(response.getWriter()).thenReturn(out);

        getLastIndicationServlet.doGet(request, response);

        verify(objectMapper).writeValueAsString(requestArgumentCaptor.capture());
        IndicationDto result = requestArgumentCaptor.getValue();

        assertThat(result.getType()).isEqualTo(indication.getType());
        assertThat(result.getValue()).isEqualTo(indication.getValue());
    }

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения последнего показания для неверного типа показания")
    void doGetTest_whenWrongType() {
        String username = "name";

        when(request.getParameter("type")).thenReturn("bb");
        when(authService.isUserExist(username)).thenReturn(true);
        when(request.getHeader("username")).thenReturn(username);
        when(indicationTypeService.getType("bb")).thenReturn(null);

        assertThatThrownBy(() -> getLastIndicationServlet.doGet(request, response))
                .isInstanceOf(WrongDataException.class);
    }
}