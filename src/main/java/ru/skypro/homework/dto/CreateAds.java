package ru.skypro.homework.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class CreateAds {

    @NotNull(message = "Description must be not null")
    @Length(min = 8, max = 255, message = "Description Max 255 Symbol")
    private String description;

    @NotNull(message = "Price must be not null")
    @Max(Integer.MAX_VALUE)
    private Integer price;

    @NotNull(message = "Title must be not null")
    @Length(min = 8, max = 255, message = "Title Max 255 Symbol")
    private String title;

}
