package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.exception_handling.FileDeleteException;
import ru.skypro.homework.service.PhotoService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("{id}")
    public void downLoadImage(@PathVariable("id") int id,
                              HttpServletResponse response) {
        Photo photo =  photoService.getPhoto(id);
        Path path = Paths.get(photo.getFilePath());
        try (InputStream in = Files.newInputStream(path);
             OutputStream out = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(photo.getMediaType());
            response.setContentLength((int) photo.getFileSize());
            in.transferTo(out);
        } catch (IOException e) {
            String message = "File path " + path + " does not exist";
            log.error(message);
            throw new FileDeleteException(message);
        }
    }
}
