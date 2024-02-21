package ru.ylab.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.domain.dto.IndicationTypeDto;
import ru.ylab.exceptions.NoRightsException;
import ru.ylab.service.IndicationTypeService;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Indication type controller.
 */
@Validated
@Loggable
@RestController
@RequestMapping(value = "/indication/type")
@RequiredArgsConstructor
public class IndicationTypeController {
    /**
     * Type service exemplar
     */
    private final IndicationTypeService indicationTypeService;

    /**
     * Add type.
     *
     * @param username          the username
     * @param indicationTypeDto the indication type dto
     * @return all types
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> addType(@RequestHeader String username,
                                                  @RequestBody @Valid IndicationTypeDto indicationTypeDto) {
        if (!username.equals("admin")) {
            throw new NoRightsException("Вы имеете недостаточно прав для выполнения данной операции");
        }

        indicationTypeService.addType(indicationTypeDto.getName());
        return ResponseEntity.status(201).body(indicationTypeService.getAllTypes());
    }

    /**
     * Gets all types.
     *
     * @return the all types
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllTypes() {
        return ResponseEntity.ok(indicationTypeService.getAllTypes());
    }
}



