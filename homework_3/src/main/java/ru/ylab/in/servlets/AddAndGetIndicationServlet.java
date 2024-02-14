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
import ru.ylab.model.Indication;
import ru.ylab.service.AuthService;
import ru.ylab.service.MonitoringService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Add indication servlet.
 */
@Loggable
public class AddAndGetIndicationServlet extends HttpServlet {
    /**
     * Mapper Json
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
        this.monitoringService = new MonitoringService();
        this.objectMapper = new ObjectMapper();
        this.authService = new AuthService();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Post indication
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String username = getValidUsername(req);
        if (username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        InputStream data = req.getInputStream();
        IndicationDto indicationDto = objectMapper.readValue(data, IndicationDto.class);
        validateIndication(indicationDto);

        Indication result = monitoringService.sendIndication(IndicationMapper.INSTANCE.indicationDtoToIndication(
                indicationDto, username, LocalDate.now()));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        String json = objectMapper.writeValueAsString(IndicationMapper.INSTANCE.indicationToDto(result));
        PrintWriter out = resp.getWriter();
        out.write(json);
    }

    /**
     * Get all indications (admin)
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = getValidUsername(req);

        if (!username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        List<IndicationDto> indications = IndicationMapper.INSTANCE.listOfIndicationToDto(
                monitoringService.getAllIndications());
        String result = objectMapper.writeValueAsString(indications);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }

    private void validateIndication(IndicationDto indicationDto) {
        if (indicationDto.getValue() == null || indicationDto.getValue() < 0) {
            throw new WrongDataException("Некорректное показание");
        }
        if (indicationDto.getType() == null) {
            throw new WrongDataException("Некорректный тип показания");
        }
    }

    /**
     * Gets valid username.
     *
     * @param req         the req
     */
    private String getValidUsername(HttpServletRequest req) {
        String username;
        try {
            username = req.getHeader("username");
        } catch (NullPointerException e) {
            throw new WrongDataException("Введите имя пользователя");
        }
        if (username == null || !authService.hasUser(username)) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }
        return username;
    }
}



