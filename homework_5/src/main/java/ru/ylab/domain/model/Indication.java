package ru.ylab.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * The class of Indication.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Indication {
    Long id;
    /**
     * Type of indication
     */
    String type;
    /**
     * Date of sent indication yyyy-MM-dd
     */
    LocalDate date;
    /**
     * Sent value
     */
    Long value;

    /**
     * Name of the user who sent the indication
     */
    String username;

    /**
     * Simplified constructor
     *
     * @param date  indication date
     * @param value indication value
     */
    public Indication(LocalDate date, Long value) {
        this.date = date;
        this.value = value;
    }
}
