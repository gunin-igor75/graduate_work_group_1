package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.util.FileManager;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Validated
public class AdsController {
    private final AdsService adsService;

    private final FileManager fileManager;

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdsDTO> createAds(@RequestPart(name = "properties") @Valid CreateAds createAds,
                                            @RequestPart(name = "image") MultipartFile file) {
        if (fileManager.checkFile(file)) {
            return ResponseEntity.badRequest().build();
        }
        AdsDTO adsDTO = adsService.createAds(createAds, file);
        return ResponseEntity.status(201).body(adsDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable int id) {
        FullAds ads = adsService.getFullAds(id);
        return ResponseEntity.ok(ads);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAds(@PathVariable int id) {
        adsService.deleteAds(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<AdsDTO> updateAds(@PathVariable int id,
                                            @RequestBody @Valid CreateAds createAds) {
        AdsDTO adsDTO = adsService.updateAds(id, createAds);
        return ResponseEntity.ok(adsDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe() {
        ResponseWrapperAds adsMe = adsService.getAdsMe();
        return ResponseEntity.ok(adsMe);
    }

    @PatchMapping(path = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePictureAds(@PathVariable int id,
                                                   @RequestPart(name = "image") MultipartFile file) {
        if (fileManager.checkFile(file)) {
            return ResponseEntity.badRequest().build();
        }
        String link = adsService.updatePictureByAds(id, file);
        return ResponseEntity.ok(link);
    }
}
