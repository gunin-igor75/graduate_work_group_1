package ru.skypro.homework.service;

import ru.skypro.homework.entity.User;

public interface UserService {
    User getUser();

    void updateAvatarService(String fileName);
}
