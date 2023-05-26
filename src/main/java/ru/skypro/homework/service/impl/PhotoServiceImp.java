package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.exception_handling.PhotoNotFoundException;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.OwnerPhoto;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.util.FileManager;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Сервис-класс определяющий логику создания, получения, изменения, удаления картинки в БД
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImp implements PhotoService {

    private final PhotoRepository photoRepository;

    private final FileManager fileManager;

    @Value("${ads.picture.dir.path}")
    private String directoryPicture;

    /**
     * Получение картики из БД по id
     *
     * @param id - индентификатор по каторому картинка хранится в БД не может быть {@code null}
     * @return - найденная картинка из БД
     * @throws PhotoNotFoundException -отсутствие картинки в БД
     */
    @Override
    public Photo getPhoto(int id) {
        return photoRepository.findById(id).orElseThrow(() -> {
                    String message = "Photo with " + id + " is not in the database";
                    log.error(message);
                    return new PhotoNotFoundException(message);
                }
        );
    }

    /**
     * Получение фото по типу фото и идентификатору хозяина
     * @param typeOwner - тип фото {@code Avatar} или {@code Picture}
     * @param id - индентификатор по каторому хозяин фото хранится в БД не может быть {@code null}
     * @return - найденное фото
     */
    @Override
    public Photo getPhotoByOwner(String typeOwner, Integer id) {
        Optional<Photo> photoOrEmpty = photoRepository.findPhotoByOwner(typeOwner, id);
        if (photoOrEmpty.isEmpty()) {
            String message = "photo with " + id +  " does not exist in the database";
            log.error(message);
            throw new PhotoNotFoundException(message);
        }
        return photoOrEmpty.get();
    }

    /**
     * Удаление картинки из БД
     * @param photo - картинка пользователя или объявления
     */
    @Override
    @Transactional
    public void deletePhoto(Photo photo) {
        String filePath = photo.getFilePath();
        fileManager.checkExistFileAndDelete(filePath);
        photoRepository.delete(photo);
    }

    /**
     * Создание картинки
     * @param photo - картинка
     * @param file - файл картинки
     * @return - сохраненная картинка в БД
     */
    @Override
    @Transactional
    public Photo createPhoto(Photo photo, MultipartFile file) {
        String filePath = downLoadFile(photo, file);
        return correctedPhoto(photo,file,filePath);
    }

    /**
     * Изменение картинки
     * @param owner - хозяин картинки
     * @param file - файл картинки
     */
    @Override
    @Transactional
    public void  updatePhoto(OwnerPhoto owner, MultipartFile file) {
        String typeOwner = owner.getTypePhoto();
        Integer id = owner.getId();
        Photo photo = getPhotoByOwner(typeOwner, id);
        String filePath = downLoadFile(photo, file);
        correctedPhoto(photo,file,filePath);
    }

    /**
     * Скачивание фала
     * @param photo - картинка
     * @param file - файл картинки
     * @return - путь где сохранена картинка
     */
    private String downLoadFile(Photo photo, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, directoryPicture);
        fileManager.checkExistFileAndDelete(photo.getFilePath());
        fileManager.upLoadFile(file, filePath);
        return filePath.toString();
    }

    /**
     * Изменение параметров картинки
     * @param photo - картинка
     * @param file - файл картинки
     * @param filePath - путь где сохранена картинка
     * @return - сохраненная картинка в БД
     */
    private Photo correctedPhoto(Photo photo, MultipartFile file, String filePath) {
        photo.setFilePath(filePath);
        photo.setFileSize(file.getSize());
        photo.setMediaType(file.getContentType());
        return photoRepository.save(photo);
    }
}
