package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ylab.domain.dto.IndicationTypeDto;
import ru.ylab.domain.model.IndicationType;


/**
 * The interface Indication type mapper.
 */
@Mapper(componentModel = "spring")
public interface IndicationTypeMapper {

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
    @Mapping(target = "id", ignore = true)
    IndicationType indicationTypeDtoToIndicationType(IndicationTypeDto indicationTypeDto);
}