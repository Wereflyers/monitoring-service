package ru.ylab.mapper;

import org.mapstruct.Mapper;
import ru.ylab.domain.dto.IndicationDto;
import ru.ylab.domain.dto.IndicationListDto;
import ru.ylab.domain.model.Indication;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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
     * Indications to list dto indication list dto.
     *
     * @param indications the indications
     * @return the indication list dto
     */
    default IndicationListDto indicationsToListDto(List<Indication> indications) {
        return new IndicationListDto(indications.stream()
                .map(this::indicationToDto)
                .collect(Collectors.toList()));
    }
}