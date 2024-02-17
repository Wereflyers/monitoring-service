package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ylab.domain.dto.UserDto;
import ru.ylab.domain.model.User;


/**
 * The interface User mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * User dto to user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDto userDto);
}
