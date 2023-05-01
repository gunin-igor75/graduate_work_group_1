package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {
    private final AdsRepository adsRepository;
    private final AdsMapper mapper;

    @Override
    public ResponseWrapperAds getAllAds() {
        List<AdsDTO> ads = adsRepository.findAll().stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }
}
