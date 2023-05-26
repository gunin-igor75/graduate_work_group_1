package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;

/**
 * Интерфейс для преобразования пользователя
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

   /**
    * Преобразование {@code Users} в {@code UserDTO}
    * @param users - пользователь в форме для работы с БД
    * @return - пользователь в форме для работы с фронтом
    */
   UserDTO userToUserDTO(Users users);

   /**
    * Преобразование {@code RegisterReq} в {@code Users}
     * @param registerReq - пользователь в форме для работы с фронтом(региситрация)
    * @return - пользователь в форме для работы с БД
    */
   @Mapping(target = "email", source = "username")
   Users registerReqToUsers(RegisterReq registerReq);
}
