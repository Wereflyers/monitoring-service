package ru.ylab.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.ylab.dto.IndicationTypeDto;
import ru.ylab.model.IndicationType;


/**
 * The interface Indication type mapper.
 */
@Mapper
@Component
public interface IndicationTypeMapper {
    /**
     * The constant INSTANCE.
     */
    IndicationTypeMapper INSTANCE = Mappers.getMapper(IndicationTypeMapper.class);

    /**
     * Indication type to dto indication type dto.
     *
     * @param indicationType the indication type
     * @return the indication type dto
     */
    IndicationTypeDto indicationTypeToDto(IndicationType indicationType);

    /**
     * Indication type dto to an indication type.
     *
     * @param indicationTypeDto the indication type dto
     * @return the indication type
     */
    IndicationType indicationTypeDtoToIndicationType(IndicationTypeDto indicationTypeDto);
}