package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.PhotoNotFoundException;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.PhotoService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImp implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public Photo getPhoto(int id) {
        return photoRepository.findById(id).orElseThrow(() -> {
                    String message = "Photo with " + id + " is not in the database";
                    log.error(message);
                    return new PhotoNotFoundException(message);
                }
        );
    }

    @Override
    public Photo getAvatarByUsersIdOrGetNew(Users user) {
        Integer userId = user.getId();
        return photoRepository.findAvatarByUsersId(userId).orElse(new Avatar(user));
    }

    @Override
    public Photo createOrUpdatePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPictureByAdsId(int adsId) {
        Optional<Photo> picture = photoRepository.findPictureByAdsId(adsId);
        if (picture.isEmpty()) {
            String message = "Photo with " + adsId + "ad is not in the database";
            log.error(message);
            throw new PhotoNotFoundException(message);
        }
        return picture.get();
    }

    @Override
    public void deletePhoto(Photo photo) {
        Integer id = photo.getId();
        Photo photoPersistent = getPhoto(id);
        photoRepository.delete(photoPersistent);
    }
}
