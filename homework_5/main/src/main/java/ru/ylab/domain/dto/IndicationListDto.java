package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IndicationListDto {
    /**
     * List of indications
     */
    private List<IndicationDto> indications;
}
