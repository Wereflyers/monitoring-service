package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The list of Indication types.
 */
@Getter
@AllArgsConstructor
public class IndicationType {
    private long id;
    /**
     * Name of types
     */
    private String name;
}
