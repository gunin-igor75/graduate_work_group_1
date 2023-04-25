package ru.skypro.homework.dto;

import lombok.*;

@Data
@Builder
public class CommentDTO {

    private Long id;

    private Long authorId;

    private String authorFirstName;

    private String authorImage;

    private Long createdAt;

    private String text;

}
