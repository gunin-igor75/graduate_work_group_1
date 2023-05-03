package ru.skypro.homework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullAds {

    private Integer pk;

    private String authorFirstName;

    private String authorLastName;

    private String description;

    private String email;

    private String image;

    private String phone;

    private Integer price;

    private String title;

}
