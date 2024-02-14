package ru.ylab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The list of Indication types.
 */
@AllArgsConstructor
@Getter
public class IndicationType extends BaseModel {
    /**
     * Name of types
     */
    private final String name;

    /**
     * All args constructor.
     *
     * @param id type id
     * @param name type name
     */
    public IndicationType(long id, String name) {
        super(id);
        this.name = name;
    }
}
