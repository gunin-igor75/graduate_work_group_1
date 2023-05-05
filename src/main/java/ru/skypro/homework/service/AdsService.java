package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;
import java.util.Collection;

public interface AdsService {
    FullAds getById(Integer id);
    ResponseWrapperAds getMeAds();

    ResponseWrapperAds getAllAds();
    AdsReq createAds (CreateAds createAds, MultipartFile file) throws IOException;
    void deleteAds (Integer id);
    AdsDTO updateAds( Integer id, CreateAds createAds);
    void updateAdsPicture(Integer id, MultipartFile file) throws IOException;

}
