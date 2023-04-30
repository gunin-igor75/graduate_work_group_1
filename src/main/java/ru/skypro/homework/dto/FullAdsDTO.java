package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FullAdsDTO {

    private Integer pk;

    private String authorFirstName;

    private  String authorLastName;

    private  String email;

    private String phone;

    private String image;

    private Integer price;

    private String  title;

    private  String description;
}
