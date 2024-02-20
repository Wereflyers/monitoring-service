package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.dto.IndicationDto;
import ru.ylab.dto.mapper.IndicationMapper;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.service.MonitoringService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Get all indications servlet.
 */
@Loggable
public class GetAllIndicationsServlet extends HttpServlet {
    /**
     * Mapper
     */
    private ObjectMapper objectMapper;
    /**
     * Monitoring service exemplar
     */
    private MonitoringService monitoringService;

    /**
     * Custom init method
     */
    @Override
    public void init() {
        this.monitoringService = new MonitoringService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Get all indications (admin)
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getHeader("username");
        if (!username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        List<IndicationDto> indications = monitoringService.getAllIndications().stream()
                .map(IndicationMapper.INSTANCE::indicationToDto)
                .collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(indications);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }
}
