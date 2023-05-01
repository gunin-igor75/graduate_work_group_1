package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.service.AdsService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;

    @GetMapping
    public ResponseEntity<Collection<AdsDTO>> getAllAds() {
        Collection<AdsDTO> allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @GetMapping("/me")
    public ResponseEntity<Collection<AdsDTO>> getAdsMe() {
        Collection<AdsDTO> allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

 }
