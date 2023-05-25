package ru.skypro.homework.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Коментарий
 */
@Data
@Builder
public class CommentDTO {

    /** Идентификатор автора коментария */
    private Integer author;

    /** Эндпоинт аватарки автора коментария*/
    private String authorImage;

    /** Имя автора коментария */
    private String authorFirstName;

    /** Колисество мили сек с 1970 */
    private Long createdAt;

    /** Идентификатор коментария */
    private Integer pk;

    /** Текст коментария */
    @NotNull(message = "Text must be not null")
    @Length(min = 8, max = 255, message = "Text Max 255 Symbol")
    private String text;

}
