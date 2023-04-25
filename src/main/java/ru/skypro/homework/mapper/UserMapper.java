package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

   UserDTO userToUserDTO(User user);

}
