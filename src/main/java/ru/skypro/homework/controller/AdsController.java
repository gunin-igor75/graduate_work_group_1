package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdsDTO> createAds(@RequestPart(name = "properties") CreateAds createAds,
                                            @RequestPart(name = "image") MultipartFile file) {
        return ResponseEntity.status(201).body(new AdsDTO());
    }

    @GetMapping("{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new FullAds());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAds(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<AdsDTO> updateAds(@PathVariable Integer id,
                                            @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(new AdsDTO());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe() {
        ResponseWrapperAds allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @PatchMapping(path = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePictureAds(@PathVariable Integer id,
                                                   @RequestPart(name = "image") MultipartFile image) {
        return ResponseEntity.ok("");
    }
}
