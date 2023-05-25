package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * Обертка коллекции объявлений
 */
@Data
@Builder
public class ResponseWrapperAds {

    /** Количество объявлений */
    private Integer count;

    /** Коллекция объявлений */
    private List<AdsDTO> results;
}
