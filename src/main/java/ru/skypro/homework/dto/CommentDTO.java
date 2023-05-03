package ru.skypro.homework.dto;

import lombok.*;

@Data
@Builder
public class CommentDTO {

    private Integer author;

    private String authorImage;

    private String authorFirstName;

    private Long createdAt;

    private Integer pk;

    private String text;

}
