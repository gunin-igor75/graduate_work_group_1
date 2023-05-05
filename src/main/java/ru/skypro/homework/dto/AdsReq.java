package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdsReq {
    Integer author;
    String image;

    Integer pk;

    String description;

    Integer price;
    String title;

}
