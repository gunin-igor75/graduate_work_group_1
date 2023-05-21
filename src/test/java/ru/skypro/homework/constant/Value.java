package ru.skypro.homework.constant;

import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;

import java.util.List;

public class Value {
    public static Users givenUsers() {
        return Users.builder()
                .id(1)
                .email("user@mail.ru")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .password("$2a$12$Dn88gLtjUPpzOfajqvrLzu9hwI/nlahRKZm9s9O4wP/n0SgWmU22S")
                .build();
    }

    public static UserDTO givenUserDTO() {
        return UserDTO.builder()
                .id(1)
                .email("user@mail.ru")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .build();
    }

    public static CreateAds givenCreateAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return CreateAds.builder()
                .description(description)
                .title(title)
                .price(price)
                .build();
    }

    public static Ads givenAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return Ads.builder()
                .id(1)
                .users(givenUsers())
                .image("/image/1")
                .title(title)
                .description(description)
                .price(price)
                .build();
    }

    public static AdsDTO givenAdsDTO() {
        String title = "add title ads";
        Integer price = 1000;
        return AdsDTO.builder()
                .author(1)
                .pk(1)
                .price(price)
                .title(title)
                .image("/image/1")
                .build();
    }

    public static FullAds givenFullAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return FullAds.builder()
                .pk(1)
                .title(title)
                .description(description)
                .authorFirstName("igor")
                .authorLastName("igoreck")
                .email("user@mail.ru")
                .price(price)
                .image("/image/1")
                .phone("+79139792520")
                .build();
    }


    public static List<Ads> givenListAds() {
        Ads ads1 = Ads.builder()
                .id(1)
                .users(givenUsers())
                .title("add title ads first")
                .description("ads description")
                .price(1000)
                .build();
        Ads ads2 = Ads.builder()
                .id(2)
                .users(givenUsers())
                .title("add title ads second")
                .description("ads description")
                .price(2000)
                .build();
        return List.of(ads1, ads2);
    }

    public static List<AdsDTO> givenListAdsDTO() {
        AdsDTO adsDTO1 = AdsDTO.builder()
                .pk(1)
                .author(1)
                .title("add title ads first")
                .price(1000)
                .build();
        AdsDTO adsDTO2 = AdsDTO.builder()
                .pk(2)
                .author(1)
                .title("add title ads second")
                .price(2000)
                .build();
        return List.of(adsDTO2, adsDTO1);
    }

    public static Photo givenPhoto() {
        return Photo.builder()
                .filePath("image/image.jpg")
                .fileSize(10000)
                .id(1)
                .mediaType("image/jpeg")
                .build();
    }
}
