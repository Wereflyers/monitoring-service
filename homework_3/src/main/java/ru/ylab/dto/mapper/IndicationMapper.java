package ru.ylab.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.ylab.dto.IndicationDto;
import ru.ylab.model.Indication;

import java.time.LocalDate;
import java.util.List;


/**
 * The interface Indication mapper.
 */
@Mapper
@Component
public interface IndicationMapper {
    /**
     * The constant INSTANCE.
     */
    IndicationMapper INSTANCE = Mappers.getMapper(IndicationMapper.class);

    /**
     * Indication to dto indication dto.
     *
     * @param indication the indication
     * @return the indication dto
     */
    IndicationDto indicationToDto(Indication indication);

    /**
     * Indication dto to indication indication.
     *
     * @param indicationDto the indication dto
     * @param username      the username
     * @param date          the date
     * @return the indication
     */
    Indication indicationDtoToIndication(IndicationDto indicationDto, String username, LocalDate date);

    /**
     * List of indication to dto list.
     *
     * @param indications the indications
     * @return list
     */
    List<IndicationDto> listOfIndicationToDto(List<Indication> indications);
}