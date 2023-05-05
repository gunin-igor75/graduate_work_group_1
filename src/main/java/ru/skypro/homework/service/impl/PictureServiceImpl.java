package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.exception_handling.PictureNotFoundException;
import ru.skypro.homework.repository.PictureRepository;
import ru.skypro.homework.service.PictureService;
@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;

    @Override
    public Picture getPictureByAdsId(Integer id) {
        return pictureRepository.getReferenceById(id);
    }

    @Override
    public void pictureUpdate(Picture picture) {
        if (picture == null) {
            throw new PictureNotFoundException();
        }
        pictureRepository.save(picture);
    }
}
