package ru.ylab.repository;

import ru.ylab.model.IndicationType;

import java.sql.SQLException;
import java.util.List;

/**
 * The interface Indication type repository.
 */
public interface IndicationTypeRepository {
    /**
     * Gets type by name.
     *
     * @param typeName the type name
     * @return the type
     * @throws SQLException the sql exception
     */
    IndicationType getTypeByName(String typeName) throws SQLException;

    /**
     * Gets all types.
     *
     * @return the all types
     * @throws SQLException the sql exception
     */
    List<String> getAllTypes() throws SQLException;

    /**
     * Add type.
     *
     * @param type the type name
     * @throws SQLException the sql exception
     */
    void addType(String type) throws SQLException;
}
