package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Объявление
 */
@Data
@Builder
public class CreateAds {

    /** Описание объявления */
    @NotNull(message = "Description must be not null")
    @Length(min = 8, max = 255, message = "Description Max 255 Symbol")
    private String description;

    /** Стоимость объявления */
    @NotNull(message = "Price must be not null")
    @Max(Integer.MAX_VALUE)
    @Min(0)
    private Integer price;

    /** Название объяления */
    @NotNull(message = "Title must be not null")
    @Length(min = 8, max = 255, message = "Title Max 255 Symbol")
    private String title;

}
