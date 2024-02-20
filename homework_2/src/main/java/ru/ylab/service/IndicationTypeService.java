package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.model.IndicationType;
import ru.ylab.repository.IndicationTypeRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Indication type service is responsible for business logic of types.
 */
@RequiredArgsConstructor
public class IndicationTypeService {
    /**
     * Injection of repository.
     */
    private final IndicationTypeRepository indicationTypeRepository;

    /**
     * Gets type.
     *
     * @param typeName the type name
     * @return the type
     */
    public IndicationType getType(String typeName) {
        try {
            return indicationTypeRepository.getTypeByName(typeName);
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all types.
     *
     * @return the all types
     */
    public List<String> getAllTypes() {
        try {
            return indicationTypeRepository.getAllTypes();
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add type.
     *
     * @param typeName the type name
     */
    public void addType(String typeName) {
        try {
            if (indicationTypeRepository.getTypeByName(typeName) != null) {
                System.out.println("Этот тип показаний уже существует");
                return;
            }
            indicationTypeRepository.addType(typeName);
        } catch (SQLException e) {
            System.out.println("SQLException occurred " + e.getSQLState());
            e.printStackTrace();
        }
    }
}
