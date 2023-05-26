package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Объявление
 */
@Data
@Builder
public class AdsDTO {

    /** Идентификатор автора объявления*/
    private Integer author;

    /** Эндпоинт картинки */
    private String image;

    /** Идентификатор объявления */
    private Integer pk;

    /** Стоимость объявления */
    private Integer price;

    /** Название объявления */
    private String  title;

}
