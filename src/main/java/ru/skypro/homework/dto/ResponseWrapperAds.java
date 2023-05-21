package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class ResponseWrapperAds {

    private Integer count;

    private List<AdsDTO> results;
}
