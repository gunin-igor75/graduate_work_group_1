package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;

public interface UserService {
    Users findUserById(Integer id);

    UserDTO getUser();

    void updateAvatarService(String fileName);

    boolean updatePassword(String currentPassword, String newPassword);

    UserDTO updateUser(RegisterReq registerReq);
}
