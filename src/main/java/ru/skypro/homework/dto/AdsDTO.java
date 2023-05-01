package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdsDTO {

    private Integer author;

    private String image;

    private Integer pk;

    private Integer price;

    private String  title;

    public AdsDTO() {
    }
}
