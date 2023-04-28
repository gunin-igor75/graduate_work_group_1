package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;


@Mapper(componentModel = "spring")
public interface UserMapper {
   UserDTO userToUserDTO(Users users);
   Users userDTOToUsers(UserDTO userDTO);
   @Mapping(target = "email", source = "username")
   Users registerReqToUsers(RegisterReq registerReq);
}
