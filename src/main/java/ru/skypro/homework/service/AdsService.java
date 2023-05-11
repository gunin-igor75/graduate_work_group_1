package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;

public interface AdsService {
    ResponseWrapperAds getAllAds();

    AdsDTO createAds(CreateAds createAds, MultipartFile file);

    Ads getAds(int id);

    FullAds getFullAds(int id);

    void deleteAds(int id);

    AdsDTO updateAds(int id, CreateAds createAds);

    ResponseWrapperAds getAdsMe();

    String updatePictureByAds(int id, MultipartFile file);

    boolean isOwnerAds(int adsId, Integer usersId);
}
