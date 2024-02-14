package ru.ylab.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.ylab.dto.UserDto;
import ru.ylab.model.User;


/**
 * The interface User mapper.
 */
@Mapper
@Component
public interface UserMapper {
    /**
     * The constant INSTANCE.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * User dto to user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    User userDtoToUser(UserDto userDto);
}
