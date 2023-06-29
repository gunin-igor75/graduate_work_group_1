package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.exception_handling.FileDeleteException;
import ru.skypro.homework.service.PhotoService;

import java.io.IOException;
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

    @GetMapping(value = "{id}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE
            })
    public ResponseEntity<byte[]> downLoadImage(@PathVariable("id") int id) {
        Photo photo = photoService.getPhoto(id);
        Path path = Paths.get(photo.getFilePath());
        byte[] bytes = getBytes(path);
        return ResponseEntity.status(HttpStatus.OK).body(bytes);
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
