package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.dto.mapper.IndicationMapper;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.Indication;
import ru.ylab.model.IndicationType;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * The type Get last indication servlet.
 */
@Loggable
public class GetLastIndicationServlet extends HttpServlet {
    /**
     * Mapper
     */
    private ObjectMapper objectMapper;
    /**
     * Monitoring service exemplar
     */
    private MonitoringService monitoringService;
    /**
     * Type service exemplar
     */
    private IndicationTypeService indicationTypeService;
    /**
     * Auth service exemplar
     */
    private AuthService authService;

    /**
     * Custom init method
     */
    @Override
    public void init() {
        this.authService = new AuthService();
        this.indicationTypeService = new IndicationTypeService();
        this.monitoringService = new MonitoringService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Get the last indication of the type for user
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getHeader("username");
        if (username == null || !authService.isUserExist(username) || username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        String typeName = req.getParameter("type");
        IndicationType indicationType = indicationTypeService.getType(typeName);
        if (indicationType == null) {
            throw new WrongDataException("Некорректный тип показания");
        }

        Indication indication = monitoringService.getLastIndication(indicationType.getId(), username);
        String result = objectMapper.writeValueAsString(IndicationMapper.INSTANCE.indicationToDto(indication));
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }
}
