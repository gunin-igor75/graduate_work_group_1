package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Photo;
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
        return photoRepository.getReferenceById(id);
    }
    @Override
    public Photo getCurrentAvatarOrNew(Integer id) {
        return photoRepository.findAvatarByUsersId(id).orElse(new Avatar());
    }

    @Override
    public Photo createPhoto(Photo photo) {
        if (photo == null) {
            throw new PhotoNotFoundException();
        }
       return photoRepository.save(photo);
    }

    @Override
    public Photo getCurrentPicture(Integer id) {
        Optional<Photo> picture = photoRepository.findPictureByAdsId(id);
        if (picture.isEmpty()) {
            throw  new PhotoNotFoundException();
        }
        return picture.get();
    }
}
