package ru.ylab.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
