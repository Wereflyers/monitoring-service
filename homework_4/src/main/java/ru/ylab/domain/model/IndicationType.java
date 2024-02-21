package ru.ylab.domain.model;

import lombok.Getter;
import ru.ylab.mapper.Default;

/**
 * The list of Indication types.
 */
@Getter
public class IndicationType extends BaseModel {
    /**
     * Name of types
     */
    private final String name;

    /**
     * Instantiates a new Indication type.
     *
     * @param id   the id
     * @param name the name
     */
    public IndicationType(long id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * Instantiates a new Indication type.
     *
     * @param name the name
     */
    @Default
    public IndicationType(String name) {
        this.name = name;
    }
}
