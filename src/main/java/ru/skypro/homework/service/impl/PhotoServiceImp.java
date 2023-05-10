package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
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
public class PhotoServiceImp implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public Photo getPhoto(int id) {
        return photoRepository.findById(id).orElseThrow(
                PhotoNotFoundException::new
        );
    }
    @Override
    public Photo getAvatarByUsersIdOrGetNew(Users user) {
        Integer userId = user.getId();
        return photoRepository.findAvatarByUsersId(userId).orElse(new Avatar(user));
    }

    @Override
    public Photo createOrUpdatePhoto(Photo photo) {
        if (photo == null) {
            throw new PhotoNotFoundException();
        }
       return photoRepository.save(photo);
    }

    @Override
    public Photo getPictureByAdsId(int adsId) {
        Optional<Photo> picture = photoRepository.findPictureByAdsId(adsId);
        if (picture.isEmpty()) {
            throw new PhotoNotFoundException();
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
