package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;
    @GetMapping("{id}")
    public ResponseEntity<FullAds> getAdsById(@PathVariable Integer id) {
        FullAds fullAds = adsService.getById(id);
        return ResponseEntity.ok(fullAds);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getMeAds() {
        ResponseWrapperAds meAds = adsService.getMeAds();
        return ResponseEntity.ok(meAds);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdsReq> createAds(@RequestPart(name = "properties") CreateAds createAds,
                                            @RequestPart(name = "image") MultipartFile file) throws IOException {
        AdsReq adsReq = adsService.createAds(createAds,file);
        return ResponseEntity.status(201).body(adsReq);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAds(@PathVariable Integer id) {
        adsService.deleteAds(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<AdsDTO> updateAds(@PathVariable Integer id,
                                            @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(new AdsDTO());
    }

    @PatchMapping(path = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePictureAds(@PathVariable Integer id,
                                                   @RequestPart(name = "image") MultipartFile image) {
        return ResponseEntity.ok("");
    }
}
