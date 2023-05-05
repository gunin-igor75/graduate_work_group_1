package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.exception_handling.AdsNotFoundException;
import ru.skypro.homework.exception_handling.FileCreateAndUpLoadException;
import ru.skypro.homework.exception_handling.FileNotException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.PictureRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PictureService;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {

    private final AdsRepository adsRepository;
    private final PictureRepository pictureRepository;
    private final PictureService pictureService;
    private final AdsMapper mapper;

    @Value("${ads.picture.dir.path}")
    private String DIRECTORY_PICTURE;
    @Value("${ads.picture.endpoint}")
    private String ENDPOINT_PICTURE;

    @Override
    public FullAds getById(Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        return FullAds.builder()
                .pk(id)
                .authorFirstName(getAuthor().getFirstName())
                .authorLastName(getAuthor().getLastName())
                .description(ads.getDescription())
                .email(getAuthor().getEmail())
                .image(ads.getImage())
                .phone(getAuthor().getPhone())
                .title(ads.getTitle())
                .build();

    }

    @Override
    public ResponseWrapperAds getMeAds() {
        List<AdsDTO> meAds = adsRepository.findByUsers(getAuthor().getId())
                .stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(meAds.size())
                .results(meAds)
                .build();
    }

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

    @Override
    @Transactional
    public AdsReq createAds(CreateAds createAds, MultipartFile file) throws IOException {
         Ads ads = new Ads();
         ads.setUsers(getAuthor());
         ads.setDescription(createAds.getDescription());
         ads.setPrice(createAds.getPrice());
         ads.setTitle(createAds.getTitle());
         Path filePath = Path.of(DIRECTORY_PICTURE, ads.getPk() + "." +
                 getExtension(Objects.requireNonNull(file.getOriginalFilename())));
         Files.createDirectories(filePath.getParent());
         uploadFile(file, filePath);
         Picture picture = new Picture();
         picture.setFilePath(filePath.toString());
         picture.setMediaType(file.getContentType());
         picture.setFileSize(file.getSize());
         pictureRepository.save(picture);
         ads.setImage(ENDPOINT_PICTURE + getAuthor().getId());
         adsRepository.save(ads);

         return AdsReq.builder()
                 .author(getAuthor().getId())
                 .image(ads.getImage())
                 .pk(ads.getPk())
                 .description(ads.getDescription())
                 .price(ads.getPrice())
                 .title(ads.getTitle())
                 .build();
    }

    @Override
    public void deleteAds(Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        adsRepository.delete(ads);
    }

    @Override
    public AdsDTO updateAds(Integer id, CreateAds createAds) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        adsRepository.save(ads);
        return mapper.adsToAdsDTO(ads);
    }

    @Override
    @Transactional
    public void updateAdsPicture(Integer id, MultipartFile file) throws IOException {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        Path filePath = Path.of(DIRECTORY_PICTURE, id + "." +
                getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        uploadFile(file,filePath);
        Picture picture = pictureService.getPictureByAdsId(id);
        checkExistFileAndDelete(picture);
        picture.setFilePath(filePath.toString());
        picture.setMediaType(file.getContentType());
        picture.setFileSize(file.getSize());
        picture.setAds(ads);
        pictureRepository.save(picture);
        ads.setImage(ENDPOINT_PICTURE + id);
        adsRepository.save(ads);
    }

    private Users getAuthor(){
        Users author =(Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return author;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    private void uploadFile(MultipartFile file, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (InputStream in = file.getInputStream();
                 OutputStream out = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(in, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(out, 1024)
            ) {
                bis.transferTo(bos);
            }
        } catch (IOException e) {
            log.error("Error create or upload file");
            throw new FileCreateAndUpLoadException();
        }
    }
    private void checkExistFileAndDelete(Picture picture) {
        String filePath = picture.getFilePath();
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new FileNotException();
            }
        }
    }
}
