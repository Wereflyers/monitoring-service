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
import ru.ylab.service.IndicationTypeService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllIndicationTypesServletTest {
    @InjectMocks
    private GetAllIndicationTypesServlet servlet;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private IndicationTypeService indicationTypeService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter out;
    @Captor
    private ArgumentCaptor<List<String>> requestArgumentCaptor;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для получения всех типов показания")
    void doGetTest() {
        List<String> expected = new ArrayList<>();
        expected.add("ГВ");
        expected.add("ХВ");
        expected.add("ОТОПЛЕНИЕ");

        when(indicationTypeService.getAllTypes()).thenReturn(expected);
        when(objectMapper.writeValueAsString(expected)).thenReturn("str");
        when(response.getWriter()).thenReturn(out);

        servlet.doGet(request, response);

        verify(objectMapper).writeValueAsString(requestArgumentCaptor.capture());
        List<String> result = requestArgumentCaptor.getValue();

        assertThat(result).isEqualTo(expected);
    }
}