package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.dto.IndicationTypeDto;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.service.IndicationTypeService;

import java.io.IOException;
import java.io.InputStream;

/**
 * The type Add indication type servlet.
 */
@Loggable
public class AddIndicationTypeServlet extends HttpServlet {
    /**
     * Mapper
     */
    private ObjectMapper objectMapper;
    /**
     * Type service exemplar
     */
    private IndicationTypeService indicationTypeService;

    /**
     * Custom init method
     */
    @Override
    public void init() {
        this.indicationTypeService = new IndicationTypeService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Post new type
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getHeader("username");
        if (!username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        InputStream data = req.getInputStream();
        IndicationTypeDto indicationTypeDto = objectMapper.readValue(data, IndicationTypeDto.class);

        indicationTypeService.addType(indicationTypeDto.getName());
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.sendRedirect(req.getContextPath() + "/indication/type/all");
    }
}



