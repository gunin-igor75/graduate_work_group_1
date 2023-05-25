package ru.skypro.homework.dto;

import lombok.*;

/**
 * Объявление
 */
@Data
@Builder
public class FullAds {

    /** Идентификатор объявления */
    private Integer pk;

    /** Имя хозяина объявления */
    private String authorFirstName;

    /** Фамилия хозяина объявления */
    private String authorLastName;

    /** Описание объявления */
    private String description;

    /** Почта хозяина объявления */
    private String email;

    /** Эндпоинт картинки оьъявления */
    private String image;

    /** Номер телефона хозяина объявления */
    private String phone;

    /** Стоимсоть объявления */
    private Integer price;

    /** Название объявления */
    private String title;

}
