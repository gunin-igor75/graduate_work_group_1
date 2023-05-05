package ru.skypro.homework.service;

import ru.skypro.homework.entity.Picture;

public interface PictureService {
    Picture getPictureByAdsId(Integer id);

    void pictureUpdate(Picture picture);
}
