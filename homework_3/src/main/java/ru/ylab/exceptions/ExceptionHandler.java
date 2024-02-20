package ru.ylab.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * The type Exception handler.
 */
public class ExceptionHandler extends HttpServlet {
	/**
	 * Mapper
	 */
	private ObjectMapper objectMapper;

	/**
	 * init handler
	 */
	@Override
	public void init() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	/**
	 * Hande method get
	 * @param request the request
	 * @param response the response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handle(request, response);
	}

	/**
	 * Hande method post
	 * @param request the request
	 * @param response the response
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handle(request, response);
	}

	/**
	 * Write info about the exception
	 * @param request request
	 * @param response response
	 * @throws IOException
	 */
	private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
		String servletName = (String) request.getAttribute("jakarta.servlet.error.servlet_name");

		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");

		if (requestUri == null) {
			requestUri = "Unknown";
		}

		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (throwable != null) {
			if (throwable.getClass() == WrongDataException.class) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else if (throwable.getClass() == NoRightsException.class) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else if (throwable.getClass() == UserAlreadyRegisteredException.class) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
			} else if (throwable.getClass() == NullPointerException.class) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

			ApiError error = new ApiError(servletName + " " + requestUri + " throws exception.",
					throwable.getMessage());

			out.write(objectMapper.writeValueAsString(error));
			throwable.printStackTrace();
		} else {
			out.write("Unknown exception");
		}
	}
}
