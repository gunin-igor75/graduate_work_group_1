package ru.skypro.homework.service;

import ru.skypro.homework.entity.Avatar;

public interface AvatarService {
    Avatar getCurrentAvatarOrNew(Integer id);

    Avatar getAvatarById(Integer id);

    void avatarCreateOrUpdate(Avatar avatar);
}
