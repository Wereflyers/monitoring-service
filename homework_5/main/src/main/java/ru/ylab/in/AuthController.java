package ru.ylab.in;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.domain.dto.UserDto;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.service.AuthService;
import ru.ylab.starter.aop.annotations.Loggable;


/**
 * The type Auth controller.
 */
@Validated
@Loggable
@RestController
@RequiredArgsConstructor
public class AuthController {
    /**
     * Auth service exemplar
     */
    private final AuthService authService;
    /**
     * Mapper
     */
    private final UserMapper userMapper;

    /**
     * Login response entity.
     *
     * @param userDto the user dto
     * @return the response entity
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        if (userDto.getUsername() != null && userDto.getPassword() != null) {
            authService.authUser(userMapper.toUser(userDto));
            String result = "Login successful. Welcome, " + userDto.getUsername();
            return ResponseEntity.ok(result);
        } else {
            throw new NoRightsException("Неверный логин / пароль");
        }
    }

    /**
     * Register response entity.
     *
     * @param userDto the user dto
     * @return the response entity
     */
    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody @Valid UserDto userDto) {
        String username = authService.registerUser(userMapper.toUser(userDto));
        String result = "Registration is successful. Welcome, " + username;
        return ResponseEntity.status(201).body(result);
    }
}
