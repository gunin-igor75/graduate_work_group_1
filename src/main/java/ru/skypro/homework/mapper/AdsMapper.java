package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.entity.Ads;

/**
 * Интерфейс для преобразований объявлений
 */

@Mapper(componentModel = "spring")
public interface AdsMapper {

    /**
     * Преобраование {@code Ads} в {@code AdsDTO}
     * @param ads - объявление в форме для работы с БД
     * @return - форма объявления для работы с фронтом не подробная
     */
    @Mapping(target = "author", source = "users.id")
    @Mapping(target = "pk", source = "id")
    AdsDTO adsToAdsDTO(Ads ads);

    /**
     * Преобраование {@code Ads} в {@code FullAds}
     * @param ads - объявление в форме для работы с БД
     * @return - форма объявления для работы с фронтом подробная
     */
    @Mapping(target = "authorFirstName", source = "users.firstName")
    @Mapping(target = "authorLastName", source = "users.lastName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "phone", source = "users.phone")
    @Mapping(target = "pk", source = "id")
    FullAds adsToFullAds(Ads ads);

    /**
     * Преобраование {@code CreateAds} в {@code Ads}
     * @param createAds - форма объявления для работы с фронтом (прием параметров)
     * @return - объявление в форме для работы с БД
     */
    Ads createAdsToAds(CreateAds createAds);

}
