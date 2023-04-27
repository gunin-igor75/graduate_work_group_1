package ru.skypro.homework.service;

import ru.skypro.homework.dto.UserDTO;

public interface UserService {
    UserDTO getUser();

    void updateAvatarService(String fileName);

    boolean updatePassword(String currentPassword, String newPassword);
}
