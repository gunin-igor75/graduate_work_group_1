package ru.skypro.homework.util;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception_handling.FileCreateAndUpLoadException;
import ru.skypro.homework.exception_handling.FileNotException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileManager {
    public boolean checkFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return file.getSize() == 0
                || filename == null
                || !Objects.requireNonNull(file.getContentType()).contains("image");
    }


    public void checkExistFileAndDelete(String filePath) {
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new FileNotException();
            }
        }
    }

    public void upLoadFile(MultipartFile file, Path filePath) {
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

    public Path getRandomPath(MultipartFile file, String directory) {
        UUID uuid = UUID.randomUUID();
        String subsequence = uuid.toString();
        String filename = file.getOriginalFilename();
        String extension = getExtension(filename);
        return Path.of(directory, subsequence + "." + extension);
    }

    private String getExtension(String fileName) {
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new FileNotException();
    }
}
