package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.exception_handling.FileDeleteException;
import ru.skypro.homework.service.PhotoService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping(value = "{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downLoadImage(@PathVariable("id") int id) {
        Photo photo = photoService.getPhoto(id);
        Path path = Paths.get(photo.getFilePath());
        byte[] bytes = getBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photo.getMediaType()));
        headers.setContentLength(photo.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytes);
    }

    private byte[] getBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            String message = "File path " + path + " does not exist";
            log.error(message);
            throw new FileDeleteException(message);
        }
    }
}
