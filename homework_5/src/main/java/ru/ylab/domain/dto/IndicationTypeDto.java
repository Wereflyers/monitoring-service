package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The class of Indication type Dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IndicationTypeDto {
    /**
     * Name of a type
     */
    @NotNull(message = "Необходимо указать название показания")
    @NotBlank(message = "Необходимо указать название показания")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-\\s]{2,20}$",
            message = "Длина названия показания должна быть от 2 до 30 символов. " +
            "Цифры в названии не допускаются.")
    private String name;
}
