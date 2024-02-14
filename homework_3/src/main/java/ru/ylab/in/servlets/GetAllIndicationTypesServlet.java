package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.service.IndicationTypeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * The type Get all indication types servlet.
 */
@Loggable
public class GetAllIndicationTypesServlet extends HttpServlet {
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
     * Get all types
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> types = indicationTypeService.getAllTypes();
        String result = objectMapper.writeValueAsString(types);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }
}
