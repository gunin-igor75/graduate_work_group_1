package ru.skypro.homework.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CommentDTO {

    private Integer author;

    private String authorImage;

    private String authorFirstName;

    private Long createdAt;

    private Integer pk;

    @NotNull(message = "Text must be not null")
    @Length(min = 8, max = 255, message = "Text Max 255 Symbol")
    private String text;

}
