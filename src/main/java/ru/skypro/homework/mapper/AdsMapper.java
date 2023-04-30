package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.entity.Ads;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "author", source = "users.id")
    AdsDTO adsToAdsDTO(Ads ads);
    FullAdsDTO adsToFullAdsDTO (Ads ads);
    Ads adsDTOToAds(AdsDTO adsDTO);

}
