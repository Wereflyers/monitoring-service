package ru.ylab;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.ylab.exceptions.NoRightsException;

import java.io.IOException;


/**
 * The type Authentication filter.
 */
@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    /**
     * Filter request
     * @param request the request
     * @param response the response
     * @param filterChain the filter
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith("/login") || path.startsWith("/registration")) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            throw new NoRightsException("Пожалуйста, авторизуйтесь");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
