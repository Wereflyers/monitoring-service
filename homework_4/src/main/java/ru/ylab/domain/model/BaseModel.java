package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base model of entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseModel {
    /**
     * Entity id
     */
    protected long id;
}
