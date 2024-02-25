package ru.ylab.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.domain.dto.IndicationDto;
import ru.ylab.domain.model.Indication;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.mapper.IndicationMapper;
import ru.ylab.service.AuthService;
import ru.ylab.service.IndicationTypeService;
import ru.ylab.service.MonitoringService;
import ru.ylab.starter.aop.annotations.Auditable;
import ru.ylab.starter.aop.annotations.Loggable;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Indication controller.
 */
@Validated
@Loggable
@Auditable
@RestController
@RequestMapping(value = "/indication")
@RequiredArgsConstructor
public class IndicationController {
    /**
     * Mapper
     */
    private final IndicationMapper indicationMapper;
    /**
     * Monitoring service exemplar
     */
    private final MonitoringService monitoringService;
    /**
     * Auth service exemplar
     */
    private final AuthService authService;

    /**
     * The Indication type service.
     */
    private final IndicationTypeService indicationTypeService;

    /**
     * Add indication response entity.
     *
     * @param username      the username
     * @param indicationDto the indication dto
     * @return the response entity
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndicationDto> addIndication(@RequestHeader String username,
                                                          @RequestBody @Valid IndicationDto indicationDto) {
        validateUser(username);

        Indication result = monitoringService.sendIndication(indicationMapper.toIndication(
                indicationDto, username, LocalDate.now()));
        return ResponseEntity.status(201).body(indicationMapper.toIndicationDto(result));
    }

    /**
     * Gets all indications.
     *
     * @param username the username
     * @return the all indications
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IndicationDto>> getAllIndications(@RequestHeader String username) {
        if (!username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }
        return ResponseEntity.ok(indicationMapper.toIndicationDtoList(monitoringService.getAllIndications()));
    }

    /**
     * Gets all indications of user.
     *
     * @param username the username
     * @return the all indications of user
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IndicationDto>> getAllIndicationsOfUser(@RequestHeader String username) {
        validateUser(username);

        return ResponseEntity.ok(indicationMapper.toIndicationDtoList(
                monitoringService.getAllIndicationsOfUser(username)));
    }

    /**
     * Gets last indication.
     *
     * @param username the username
     * @param type     the type
     * @param month    the month (optional)
     * @return the last indication
     */
    @GetMapping(value = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndicationDto> getLastIndication(@RequestHeader String username,
                                                           @RequestParam String type,
                                                           @RequestParam(required = false) Integer month) {
        Indication result;

        validateUser(username);

        IndicationType indicationType = indicationTypeService.getType(type);
        if (indicationType == null) {
            throw new WrongDataException("Некорректный тип показания");
        }

        if (month != null) {
            if (month < 1 || month > 12) {
                throw new WrongDataException("Введите число месяца");
            }
            result = monitoringService.getIndicationForMonth(indicationType.getId(), username, month);
        } else {
            result = monitoringService.getLastIndication(indicationType.getId(), username);
        }

        return ResponseEntity.ok(indicationMapper.toIndicationDto(result));
    }

    private void validateUser(String username) {
        if (!authService.hasUser(username) || username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }
    }
}



