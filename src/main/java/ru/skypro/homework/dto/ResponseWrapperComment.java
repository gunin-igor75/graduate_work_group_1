package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Обертка для коллекции коментариев
 */
@Data
@Builder
public class ResponseWrapperComment {

    /** Количество коментариев */
    private Integer count;

    /** Коллекция коментариев */
    private List<CommentDTO> results;

}
