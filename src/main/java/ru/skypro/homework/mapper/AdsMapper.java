package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.entity.Ads;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "author", source = "users.id")
    AdsDTO adsToAdsDTO(Ads ads);
    Ads adsDTOToAds(AdsDTO adsDTO);

    Ads createAdsToAds(CreateAds createAds);
    @Mapping(target = "authorFirstName", source = "users.firstName")
    @Mapping(target = "authorLastName", source = "users.lastName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "phone", source = "users.phone")
    FullAds adsToFullAds(Ads ads);

}
