package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис-класс определяющий логику создания, получения, изменения, удаления объявлений в БД
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {

    private final AdsRepository adsRepository;

    private final PhotoService photoService;

    private final UserService userService;

    private final AdsMapper mapper;

    @Value("${image.endpoint}")
    private String endpointImage;


    /**
     * Получение всех объявлений из БД в остортированном порядке - {@code title}
     * @return - сущность обертка содержит количество и сами объявления
     */
    @Override
    public ResponseWrapperAds getAllAds() {
        List<AdsDTO> ads = adsRepository.findAll().stream()
                .sorted()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    /**
     * Создание объявления
     * @param createAds - входные параметры для создания обявления
     * @param file - файл картинки объявления
     * @return - объявление после сохранения в БД преобразованное в {@code AdsDTO}
     */
    @Override
    @Transactional
    public AdsDTO createAds(CreateAds createAds, MultipartFile file) {
        Ads owner = mapper.createAdsToAds(createAds);
        Users user = userService.getUser();
        Photo photo = photoService.createPhoto(owner, file);
        owner.setUsers(user);
        owner.setImage(endpointImage + photo.getId());
        Ads ads  = adsRepository.save(owner);
        return mapper.adsToAdsDTO(ads);
    }

    /**
     * Получение объявления из БД по id
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @return - найденное объявление
     * @throws AdsNotFoundException - если объявление не найдено
     */
    @Override
    public Ads getAds(int id) {
        Optional<Ads> adsOrEmpty = adsRepository.findById(id);
        if (adsOrEmpty.isEmpty()) {
            String message = "Ad with " + id + " is not in the database";
            log.error(message);
            throw new AdsNotFoundException(message);
        }
        return adsOrEmpty.get();
    }

    /**
     * Получение объявления в подробной форме
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @return - объявление в подробной форме
     */
    @Override
    public FullAds getFullAds(int id) {
        Ads ads = getAds(id);
        return mapper.adsToFullAds(ads);
    }

    /**
     * Удаление объявления по id
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     */
    @Override
    @Transactional
    public void deleteAds(int id) {
        Ads ads = getAds(id);
        Photo photo = photoService.getPhotoByOwner(ads.getTypePhoto(), id);
        photoService.deletePhoto(photo);
        adsRepository.delete(ads);
    }

    /**
     * Обновление объявления
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @param createAds - {@code CreateAds} входные параметры для создания обявления
     * @return - объявление сохраненное в БД преобразованное в {@code AdsDTO}
     */
    @Override
    public AdsDTO updateAds(int id, CreateAds createAds) {
        Ads ads = getAds(id);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        adsRepository.save(ads);
        return mapper.adsToAdsDTO(ads);
    }

    /**
     * Получение объявлений зарегестрированного пользователя в остортированном порядке - {@code title}
     * @return - сущность обертка содержит количество и сами объявления
     */
    @Override
    public ResponseWrapperAds getAdsMe() {
        Users user = userService.getUser();
        Integer id = user.getId();
        List<AdsDTO> adsMe = adsRepository.findAdsByUserId(id)
                .stream()
                .sorted()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(adsMe.size())
                .results(adsMe)
                .build();
    }

    /**
     * Создание или обновление картинки объявления
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @param file - файл картинки объявления
     * @return - ендпоинт по которому отображается картинка объявления
     */
    @Override
    public String updatePictureByAds(int id, MultipartFile file) {
        Ads ads = getAds(id);
        photoService.updatePhoto(ads, file);
        return ads.getImage();
    }

    /**
     * Является ли пользователь хозяином объявления
     * @param adsId - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @param usersId - индентификатор по каторому пользователь хранится в БД не может быть {@code null}
     * @return - {@code true} - успех,  {@code false} - неудача
     */
    @Override
    public boolean isOwnerAds(int adsId, Integer usersId) {
        Optional<Ads> adsOrEmpty = adsRepository.findAdsByIdAndUsersId(adsId, usersId);
        return adsOrEmpty.isPresent();
    }
}
