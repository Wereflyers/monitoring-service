package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.aop.annotations.Loggable;
import ru.ylab.domain.model.IndicationType;
import ru.ylab.exceptions.SomeSQLException;
import ru.ylab.exceptions.WrongDataException;
import ru.ylab.repository.IndicationTypeRepository;
import ru.ylab.service.IndicationTypeService;

import java.sql.SQLException;
import java.util.List;

@Loggable
@Service
@RequiredArgsConstructor
public class IndicationTypeServiceImpl implements IndicationTypeService {
    /**
     * Injection of repository.
     */
    private final IndicationTypeRepository indicationTypeRepository;

    @Override
    public IndicationType getType(String typeName) {
        try {
            return indicationTypeRepository.getTypeByName(typeName);
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
    public List<String> getAllTypes() {
        try {
            return indicationTypeRepository.getAllTypes();
        } catch (SQLException e) {
            throw new SomeSQLException("SQLException occurred " + e.getSQLState());
        }
    }

    @Override
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
