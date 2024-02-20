package ru.ylab.dto;

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
    String type;
    /**
     * Sent value
     */
    Long value;
}
