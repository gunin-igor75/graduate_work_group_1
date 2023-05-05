package ru.skypro.homework.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class CommentReq {

    @NotNull(message = "Text must be not null")
    @Length(min = 2, max = 255, message = "Text Max 255 Symbol")
    private String text;
}
