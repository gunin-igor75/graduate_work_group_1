package ru.skypro.homework.service;

import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;

public interface PhotoService {
    Photo getPhoto(int id);

    Photo getAvatarByUsersIdOrGetNew(Users users);

    Photo createOrUpdatePhoto(Photo photo);

    Photo getPictureByAdsId(int adsId);

    void deletePhoto(Photo photo);
}
