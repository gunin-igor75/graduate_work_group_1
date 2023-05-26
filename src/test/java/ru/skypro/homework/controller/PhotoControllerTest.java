package ru.skypro.homework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.exception_handling.PhotoNotFoundException;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.impl.PhotoServiceImp;
import ru.skypro.homework.util.FileManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Value.givenPhotoTest;

class PhotoControllerTest extends ControllerClassTest{

    @SpyBean
    private PhotoServiceImp photoService;

    @MockBean
    private PhotoRepository photoRepository;

    @MockBean
    private FileManager fileManager;

    @Test
    @WithAnonymousUser
    @DisplayName("downLoadImage - скачивание картинки для фронта in id out status 200")
    public void downLoadImageStatus200Test() throws Exception {
        int id = 1;

        byte[] bytes = "photoControllerTest".getBytes();
        MockMultipartFile file =
                new MockMultipartFile("image", "image.jpg", IMAGE_JPEG.toString(),bytes);
        String pathFile = "src/test/" + file.getOriginalFilename();
        Photo photo = givenPhotoTest(file);

        Path path = Path.of(pathFile);
        Files.write(path, bytes);

        when(photoRepository.findById(id)).thenReturn(Optional.of(photo));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/image/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        Files.delete(path);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("downLoadImage - скачивание картинки для фронта in id out status 500")
    public void downLoadImageStatus500Test() throws Exception {
        int id = 1;

        byte[] bytes = "photoControllerTest".getBytes();
        MockMultipartFile file =
                new MockMultipartFile("image", "image.jpg", IMAGE_JPEG.toString(),bytes);
        Photo photo = givenPhotoTest(file);

        when(photoRepository.findById(id)).thenReturn(Optional.of(photo));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/image/{id}", id))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("downLoadImage - скачивание картинки для фронта in id out status 404")
    public void downLoadImageStatus404Test() throws Exception {
        int id = 1;

        when(photoRepository.findById(id)).thenThrow(PhotoNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/image/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
}