package ru.ylab.mapper;

import org.mapstruct.Mapper;
import ru.ylab.domain.dto.IndicationDto;
import ru.ylab.domain.model.Indication;

import java.time.LocalDate;
import java.util.List;


/**
 * The interface Indication mapper.
 */
@Mapper(componentModel = "spring")
public interface IndicationMapper {

    /**
     * Indication to dto indication dto.
     *
     * @param indication the indication
     * @return the indication dto
     */
    IndicationDto toIndicationDto(Indication indication);

    /**
     * Indication dto to indication indication.
     *
     * @param indicationDto the indication dto
     * @param username      the username
     * @param date          the date
     * @return the indication
     */
    Indication toIndication(IndicationDto indicationDto, String username, LocalDate date);

    /**
     * To indication dto list.
     *
     * @return the list
     */
    List<IndicationDto> toIndicationDtoList(List<Indication> indications);
}