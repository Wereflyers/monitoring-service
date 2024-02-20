package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.dto.IndicationTypeDto;
import ru.ylab.service.IndicationTypeService;

import java.io.InputStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddIndicationTypeServletTest {
    @InjectMocks
    private AddIndicationTypeServlet servlet;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private IndicationTypeService indicationTypeService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    @SneakyThrows
    @DisplayName(value = "Тест метода для добавления типа показания")
    void doPostTest() {
        IndicationTypeDto indicationTypeDto = new IndicationTypeDto("new");

        when(request.getInputStream()).thenReturn(null);
        when(request.getHeader("username")).thenReturn("admin");
        when(objectMapper.readValue((InputStream) null, IndicationTypeDto.class)).thenReturn(indicationTypeDto);

        servlet.doPost(request, response);

        verify(indicationTypeService).addType("new");
    }
}