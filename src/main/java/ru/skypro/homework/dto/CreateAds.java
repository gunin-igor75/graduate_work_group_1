package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAds {

    private String description;

    private Integer price;

    private String title;

}
