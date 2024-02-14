package ru.ylab.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.dto.UserDto;
import ru.ylab.dto.mapper.UserMapper;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.service.AuthService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * The type Registration servlet.
 */
@Loggable
@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    /**
     * Mapper
     */
    private ObjectMapper objectMapper;
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
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Register
     * @param req request
     * @param resp response
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        InputStream data = req.getInputStream();
        UserDto userDto = objectMapper.readValue(data, UserDto.class);
        validateUser(userDto);

        String username = authService.registerUser(UserMapper.INSTANCE.userDtoToUser(userDto));
        String result = "Registration is successful. Welcome, " + username;
        resp.setContentType("application/json; charset=UTF-8");
        HttpSession userSession = req.getSession();
        userSession.setAttribute("name", userDto.getUsername());
        resp.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter out = resp.getWriter();
        out.write(result);
    }

    private void validateUser(UserDto userDto) {
        if (userDto.getUsername().isBlank() || userDto.getUsername().length() < 2
                || userDto.getUsername().length() > 30) {
            throw new WrongDataException("Некорректный логин");
        }
        if (userDto.getPassword().isBlank() || userDto.getPassword().length() < 2
                || userDto.getUsername().length() > 30) {
            throw new WrongDataException("Некорректный пароль");
        }
    }
}
