package ru.skypro.homework.service;

import ru.skypro.homework.entity.Photo;

public interface PhotoService {
    Photo getPhoto(int id);

    Photo getCurrentAvatarOrNew(Integer id);

    Photo createPhoto(Photo photo);

    Photo getCurrentPicture(Integer id);
}
