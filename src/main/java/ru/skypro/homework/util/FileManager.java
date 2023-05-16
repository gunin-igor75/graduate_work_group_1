package ru.skypro.homework.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception_handling.FileCreateAndUpLoadException;
import ru.skypro.homework.exception_handling.FileDeleteException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileManager {

    public void checkFile(MultipartFile file) {
        if (!isGoodFile(file)) {
            String message = "file size zero or no picture";
            log.error(message);
            throw new FileCreateAndUpLoadException(message);
        }
    }

    public void checkExistFileAndDelete(String filePath) {
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                String message = "File path" + filePath + "does not exist";
                log.error(message);
                throw new FileDeleteException(message);
            }
        }
    }

    public void upLoadFile(MultipartFile file, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            String message = "Error create or upload file";
            log.error(message);
            throw new FileCreateAndUpLoadException(message);
        }
    }

    public Path getRandomPath(MultipartFile file, String directory) {
        UUID uuid = UUID.randomUUID();
        String subsequence = uuid.toString();
        String filename = file.getOriginalFilename();
        String extension = getExtension(filename);
        return Path.of(directory, subsequence + "." + extension);
    }

    private boolean isGoodFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return file.getSize() == 0
                || filename == null
                || !Objects.requireNonNull(file.getContentType()).contains("image");
    }

    private String getExtension(String fileName) {
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String message = "File is null";
        log.error(message);
        throw new FileCreateAndUpLoadException(message);
    }
}
