package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.service.AdsService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController("/ads")
public class AdsController {
    private final AdsService adsService;

    @GetMapping("{id}")
    public ResponseEntity<FullAdsDTO> getAdsDTO(@PathVariable Integer id) {
        FullAdsDTO fullAdsDTO = adsService.getById(id);
        return ResponseEntity.ok(fullAdsDTO);
    }

    @GetMapping
    public ResponseEntity<Collection<AdsDTO>> getAllAds() {
        Collection<AdsDTO> allAds = adsService.getAllAds();
        return ResponseEntity.ok(allAds);
    }

    @GetMapping("/me")
    public ResponseEntity<Collection<AdsDTO>> getMeAds() {
        Collection<AdsDTO> meAds = adsService.getMeAds();
        return ResponseEntity.ok(meAds);
    }
    @PostMapping
    public ResponseEntity<?> adAds (@RequestParam String image,
                                    @RequestParam Integer price,
                                    @RequestParam String title,
                                    @RequestParam String description) {

        return ResponseEntity.ok(adsService.createAds(image, price, title, description));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ads> deleteAds (@PathVariable Integer id) {
        adsService.deleteAds(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity <AdsDTO> updateAds (@PathVariable Integer id,
                                              @RequestParam Integer price,
                                              @RequestParam String title,
                                              @RequestParam String description){

        return ResponseEntity.ok(adsService.updateAds(id,price,title,description));
    }

    @PatchMapping(path =  "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity <String> updateAdsImage (@PathVariable Integer id,
                                                    @RequestPart (name = "image") MultipartFile image){
        String imageName = image.getOriginalFilename();
        adsService.updateAdsImage(id,imageName);
        return ResponseEntity.ok(imageName);
    }
}
