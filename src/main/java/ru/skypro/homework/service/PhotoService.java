package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;

import javax.transaction.Transactional;

public interface PhotoService {
    Photo getPhoto(int id);

    void deletePhoto(Photo photo);

    Photo createPhoto(OwnerPhoto owner, MultipartFile file);

    void updatePhoto(OwnerPhoto owner, MultipartFile file);

    Photo getPhotoByOwner(String typeOwner, Integer id);
}
