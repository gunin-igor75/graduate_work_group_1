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
import ru.skypro.homework.entity.*;
import ru.skypro.homework.exception_handling.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileManager;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {

    private final AdsRepository adsRepository;

    private final FileManager fileManager;

    private final PhotoService photoService;

    private final UserService userService;

    private final AdsMapper mapper;

    @Value("${ads.picture.dir.path}")
    private String directoryPicture;

    @Value("${image.endpoint}")
    private String endpointImage;


    @Override
    public ResponseWrapperAds getAllAds() {
        List<AdsDTO> ads = adsRepository.findAll().stream()
                .map(mapper::adsToAdsDTO)
                .sorted()
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    @Override
    @Transactional
    public AdsDTO createAds(CreateAds createAds, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, directoryPicture);
        Ads ads = mapper.createAdsToAds(createAds);
        Users user = userService.getUser();
        ads.setUsers(user);
        Ads persistentAds = adsRepository.save(ads);
        Photo picture = createPicture(persistentAds, file, filePath);
        persistentAds.setImage(endpointImage + picture.getId());
        Ads newAds = adsRepository.save(persistentAds);
        fileManager.upLoadFile(file, filePath);
        return mapper.adsToAdsDTO(newAds);
    }

    @Override
    public Ads getAds(int id) {
        Optional<Ads> adsOrEmpty = findAds(id);
        if (adsOrEmpty.isEmpty()) {
            String message = "Ad with " + id + " is not in the database";
            log.error(message);
            throw new AdsNotFoundException(message);
        }
        return adsOrEmpty.get();
    }

    @Override
    public FullAds getFullAds(int id) {
        Ads ads = getAds(id);
        return mapper.adsToFullAds(ads);
    }

    @Override
    @Transactional
    public void deleteAds(int id) {
        Ads ads = getAds(id);
        int photoId = getPhotoId(ads.getImage());
        Photo photo = photoService.getPhoto(photoId);
        photoService.deletePhoto(photo);
        // TODO commentService.deleteCommentsByAdsId(id) проверить при удалении объявления;
        adsRepository.delete(ads);
    }

    @Override
    public AdsDTO updateAds(int id, CreateAds createAds) {
        Ads ads = getAds(id);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        adsRepository.save(ads);
        return mapper.adsToAdsDTO(ads);
    }

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

    @Override
    public String updatePictureByAds(int id, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, directoryPicture);
        Photo picture = photoService.getPictureByAdsId(id);
        fileManager.checkExistFileAndDelete(picture.getFilePath());
        picture.setFilePath(filePath.toString());
        picture.setFileSize(file.getSize());
        picture.setMediaType(file.getContentType());
        photoService.createOrUpdatePhoto(picture);
        fileManager.upLoadFile(file, filePath);
        Ads ads = getAds(id);
        return ads.getImage();
    }

    private Optional<Ads> findAds(int id) {
        return adsRepository.findById(id);
    }
    
    @Override
    public boolean isOwnerAds(int adsId, Integer usersId) {
        Optional<Ads> adsOrEmpty = adsRepository.findAdsByIdAndUsersId(adsId, usersId);
        return adsOrEmpty.isPresent();
    }

    private Photo createPicture(Ads ads, MultipartFile file, Path path) {
        Picture picture = new Picture();
        picture.setAds(ads);
        picture.setFilePath(path.toString());
        picture.setFileSize(file.getSize());
        picture.setMediaType(file.getContentType());
        return photoService.createOrUpdatePhoto(picture);
    }

    private int getPhotoId(String endpoint) {
        String number = endpoint.substring(endpoint.lastIndexOf("/") + 1);
        return Integer.parseInt(number);
    }
}
