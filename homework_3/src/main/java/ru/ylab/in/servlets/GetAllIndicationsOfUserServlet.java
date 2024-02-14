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
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * The type Get all indications of user servlet.
 */
@Loggable
public class GetAllIndicationsOfUserServlet extends HttpServlet {
    /**
     * Mapper
     */
    private ObjectMapper objectMapper;
    /**
     * Monitoring service exemplar
     */
    private MonitoringService monitoringService;
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
        this.monitoringService = new MonitoringService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Get all indications of the user
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username;
        try {
            username = req.getHeader("username");
        } catch (NullPointerException e) {
            throw new WrongDataException("Введите имя пользователя");
        }
        if (username == null || !authService.hasUser(username) || username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        List<IndicationDto> indications = IndicationMapper.INSTANCE.listOfIndicationToDto(
                monitoringService.getAllIndicationsOfUser(username));
        String result = objectMapper.writeValueAsString(indications);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }
}
