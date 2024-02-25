package ru.ylab.service;

import ru.ylab.domain.model.IndicationType;

import java.util.List;

/**
 * Indication type service is responsible for business logic of types.
 */
public interface IndicationTypeService {
    /**
     * Gets type.
     *
     * @param typeName the type name
     * @return the type
     */
    IndicationType getType(String typeName);

    /**
     * Gets all types.
     *
     * @return the all types
     */
    List<String> getAllTypes();

    /**
     * Add type.
     *
     * @param typeName the type name
     */
    void addType(String typeName);
}
