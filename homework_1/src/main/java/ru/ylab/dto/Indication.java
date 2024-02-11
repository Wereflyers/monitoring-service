package ru.ylab.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * The class of Indication.
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Indication {
    /**
     * Type of indication
     */
    String type;
    /**
     * Date of sent indication yyyy-MM-dd
     */
    final LocalDate date;
    /**
     * Sent value
     */
    final Long value;
}
