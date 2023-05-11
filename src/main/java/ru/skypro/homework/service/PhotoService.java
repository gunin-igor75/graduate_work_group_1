package ru.skypro.homework.service;

import ru.skypro.homework.entity.Photo;

public interface PhotoService {
    Photo getPhoto(int id);

    Photo getCurrentAvatarOrNew(int id);

    Photo createPhoto(Photo photo);

    Photo getCurrentPicture(Integer id);

    void deletePhoto(Photo photo);
}
