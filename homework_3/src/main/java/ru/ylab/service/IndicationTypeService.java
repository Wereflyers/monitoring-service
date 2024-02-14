package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.exceptions.SomeSQLException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.model.IndicationType;
import ru.ylab.repository.IndicationTypeRepository;
import ru.ylab.repository.IndicationTypeRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Indication type service is responsible for business logic of types.
 */
@Loggable
@RequiredArgsConstructor
public class IndicationTypeService {
    /**
     * Injection of repository.
     */
    private final IndicationTypeRepository indicationTypeRepository;

    /**
     * Instantiates a new Indication type service.
     */
    public IndicationTypeService() {
        this.indicationTypeRepository = new IndicationTypeRepositoryImpl();
    }

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
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
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
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
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
                throw new WrongDataException("Этот тип показаний уже существует");
            }
            indicationTypeRepository.addType(typeName);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }
}
