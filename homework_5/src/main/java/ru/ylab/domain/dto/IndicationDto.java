package ru.ylab.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The class of Indication Dto.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class IndicationDto {
    /**
     * Type of indication
     */
    @NotNull(message = "Введите тип показания")
    @NotBlank(message = "Введите тип показания")
    String type;
    /**
     * Sent value
     */
    @NotNull(message = "Введите показание")
    @Min(value = 1, message = "Некорректное показание")
    Long value;
}
