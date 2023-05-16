package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "author", source = "users.id")
    @Mapping(target = "pk", source = "id")
    AdsDTO adsToAdsDTO(Ads ads);
    @Mapping(target = "authorFirstName", source = "users.firstName")
    @Mapping(target = "authorLastName", source = "users.lastName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "phone", source = "users.phone")
    @Mapping(target = "pk", source = "id")
    FullAds adsToFullAds(Ads ads);

    Ads createAdsToAds(CreateAds createAds);

}
