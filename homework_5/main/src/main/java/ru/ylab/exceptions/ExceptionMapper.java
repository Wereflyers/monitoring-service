package ru.ylab.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Exception mapper.
 */
public class ExceptionMapper {
    private static final Map<String, Integer> exceptionHttpStatusMap = new HashMap<>();

    static {
        exceptionHttpStatusMap.put("NullPointerException", 404);
        exceptionHttpStatusMap.put("BadRequestException", 400);
        exceptionHttpStatusMap.put("DBException", 200);
        exceptionHttpStatusMap.put("NoRightsException", 403);
        exceptionHttpStatusMap.put("UserAlreadyRegisteredException", 409);
        exceptionHttpStatusMap.put("WrongDataException", 400);
    }

    /**
     * Gets http status by exception name.
     *
     * @param exceptionName the exception name
     * @return the http status by exception name
     */
    public static HttpStatus getHttpStatusByExceptionName(String exceptionName) {
        int status = exceptionHttpStatusMap.getOrDefault(exceptionName, 500);
        return HttpStatus.valueOf(status);
    }
}
