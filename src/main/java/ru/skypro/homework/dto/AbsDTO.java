package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbsDTO {

    private Long authorId;

    private Long id;

    private String image;

    private  Integer price;

    private String  title;

}
