package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;

import java.util.Optional;

public interface UserService {

    UserDTO getUserDTO();

    void createOrUpdateAvatar(MultipartFile image);

    void createUsers(Users user);

    Optional<Users> findUserByEmail(String email);

    boolean updatePassword(String currentPassword, String newPassword);

    UserDTO updateUser(UserDTO userDTO);

    Users getUser();

    boolean checkUserUpdate(UserDTO userDTO);
}
