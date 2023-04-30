package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Users;

import java.util.Collection;

public interface AdsService {
    FullAdsDTO getById(Integer id);
    Collection <AdsDTO> getAllAds();
    Collection <AdsDTO> getMeAds();
    AdsDTO createAds(String image, Integer price, String  title, String description);
    void deleteAds(Integer id);
    AdsDTO updateAds(Integer id, Integer price, String title, String description);
    String updateAdsImage(Integer id,String image);

}
