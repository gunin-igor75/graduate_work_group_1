package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ResponseWrapperAds {

    private Integer count;

    private Collection<AdsDTO> results;
}
