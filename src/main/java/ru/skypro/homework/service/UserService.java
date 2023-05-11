package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;

public interface UserService {
    Users findUserById(Integer id);

    UserDTO getUser();

    void createOrUpdateAvatar(MultipartFile image);

    boolean updatePassword(String currentPassword, String newPassword);

    UserDTO updateUser(UserDTO userDTO);

    Users getAuthorizedUser();
}
