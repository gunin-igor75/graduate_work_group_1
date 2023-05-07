package ru.skypro.homework.dto;


import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


@Data
@Builder
public class CommentReq {

    @NotNull(message = "Text must be not null")
    @Length(min = 2, max = 255, message = "Text Max 255 Symbol")
    private String text;
}
