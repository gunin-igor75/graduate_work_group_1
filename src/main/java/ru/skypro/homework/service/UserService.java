package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;

public interface UserService {
    Users findUserById(Integer id);

    UserDTO getUser();

    UserDTO createOrUpdateAvatar(MultipartFile image);

    void createOrUpdateUsers(Users user);

    Users getUsersByEmail(String email);

    boolean isRegistrationrUser(String email);

    boolean updatePassword(String currentPassword, String newPassword);

    UserDTO updateUser(UserDTO userDTO);

    Users getAuthorizedUser();
}
