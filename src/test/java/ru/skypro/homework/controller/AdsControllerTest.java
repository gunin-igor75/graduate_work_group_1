package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileManager;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.util.Value.*;

class AdsControllerTest extends ControllerClassTest{

    @SpyBean
    private AdsService adsService;

    @SpyBean
    private FileManager fileManager;

    @MockBean
    private AdsRepository adsRepository;

    @SpyBean
    private PhotoService photoService;

    @SpyBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PhotoRepository photoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdsMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    @DisplayName("getAllAds - вывести все объявления out ResponseWrapperAds status 200")
    public void getAllAdsStatusTest() throws Exception {
        List<Ads> ads = givenListAds();

        List<AdsDTO> adsDTO = givenListAdsDTO();

        ResponseWrapperAds wrapperAds = givenResponseWrapperAds(adsDTO);

        when(adsRepository.findAll()).thenReturn(ads);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wrapperAds)));
    }

    @Test
    @WithMockUser
    @DisplayName("createAds - создание объявления in file CreateAds out AdsDTO status 201")
    public void createAdsStatus201Test() throws Exception {
        Users users = givenUsers();

        CreateAds createdAds = givenCreateAds();

        byte[] bytesImage = "ads cat".getBytes();
        byte[] bytesCreateAds = objectMapper.writeValueAsBytes(createdAds);

        MockPart partFile = new MockPart("image", "image.jpg", bytesImage);
        partFile.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        MockPart partCreateAds = new MockPart("properties", "createAds", bytesCreateAds);
        partCreateAds.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        AdsDTO adsDTO = givenAdsDTO();

        Ads ads = givenAds();

        Photo photo = givenPhoto();

        ArgumentCaptor<Photo> photoArgCap = ArgumentCaptor.forClass(Photo.class);

        doReturn(users).when(userService).getUser();

        when(adsRepository.save(any(Ads.class))).thenReturn(ads);

        when(photoRepository.save(photoArgCap.capture())).thenReturn(photo);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST,"/ads")
                .part(partCreateAds)
                .part(partFile))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.pk").value(adsDTO.getPk()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()))
                .andExpect(jsonPath("$.image").value(adsDTO.getImage()));

        Photo actualePhoto = photoArgCap.getValue();
        fileManager.checkExistFileAndDelete(actualePhoto.getFilePath());

    }

    @Test
    @WithMockUser
    @DisplayName("createAds - создание объявления  in file CreateAds out status 400")
    public void createAdsStatus400Test() throws Exception {
        Users users = givenUsers();

        CreateAds createdAds = givenCreateAdsBad();

        byte[] bytesImage = "ads cat".getBytes();
        byte[] bytesCreateAds = objectMapper.writeValueAsBytes(createdAds);

        MockPart partFile = new MockPart("image", "image.jpg", bytesImage);
        partFile.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        MockPart partCreateAds = new MockPart("properties", "createAds", bytesCreateAds);
        partCreateAds.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST,"/ads")
                        .part(partCreateAds)
                        .part(partFile))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("getAds - получить объявление in id  out FullAds status 200")
    public void getAdsStatus200Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Ads ads = givenAds();

        FullAds fullAds = givenFullAds();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/ads/{id}", pk))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(fullAds.getPk()))
                .andExpect(jsonPath("$.authorFirstName").value(fullAds.getAuthorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(fullAds.getAuthorLastName()))
                .andExpect(jsonPath("$.price").value(fullAds.getPrice()))
                .andExpect(jsonPath("$.title").value(fullAds.getTitle()))
                .andExpect(jsonPath("$.description").value(fullAds.getDescription()))
                .andExpect(jsonPath("$.phone").value(fullAds.getPhone()))
                .andExpect(jsonPath("$.image").value(fullAds.getImage()))
                .andExpect(jsonPath("$.email").value(fullAds.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("getAds - получить объявление in id out status 404")
    public void getAdsStatus404Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(pk)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", pk))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteAds удаление объявления in id out status 204")
    public void deleteAdsTesStatusNoContent() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Ads ads = givenAds();

        Photo photo = givenPhoto();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(photoRepository.findPhotoByOwner(ads.getTypePhoto(), pk)).thenReturn(Optional.of(photo));

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/ads/{id}", pk))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("updateAds - обновление ads in CreateAds out AdsDTO status 200")
    public void updateAdsTestStatus200() throws Exception{
        Users users = givenUsers();

        int pk = 1;

        JSONObject jsonCreateAds = givenJsonCreateAds();

        Ads ads = givenAds();

        AdsDTO adsDTO = givenAdsDTO();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(adsRepository.save(ads)).thenReturn(ads);

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/ads/{id}", pk)
                .content(jsonCreateAds.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.pk").value(adsDTO.getPk()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()))
                .andExpect(jsonPath("$.image").value(adsDTO.getImage()));
    }

    @Test
    @WithMockUser
    @DisplayName("getAdsMe - показать ads зарегестрированного user out ResponseWrapperAds status 200")
    public void getAdsMeStatus200Test() throws Exception {
        Users users = givenUsers();

        List<Ads> ads = givenListAds();

        List<AdsDTO> adsDTO = givenListAdsDTO();

        ResponseWrapperAds wrapperAds = givenResponseWrapperAds(adsDTO);

        doReturn(users).when(userService).getUser();

        when(adsRepository.findAdsByUserId(users.getId())).thenReturn(ads);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wrapperAds)));
    }

    @Test
    @WithMockUser
    @DisplayName("updatePictureAds - обновление фото ads in id file out link status 200")
    public void updatePictureAdsStatus200Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Photo photo = givenPhoto();

        Ads ads = givenAds();

        byte[] bytes = "test adsController".getBytes();
        MockMultipartFile mockFile  =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        doReturn(users).when(userService).getUser();

        when(photoRepository.findPhotoByOwner(ads.getTypePhoto(), pk)).thenReturn(Optional.of(photo));

        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH,"/ads/{id}/image", pk)
                        .file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/image/1")));

        fileManager.checkExistFileAndDelete(photo.getFilePath());
    }

    @Test
    @WithMockUser
    @DisplayName("updatePictureAds - обновление фото ads in id file out link status 200")
    public void updatePictureAdsStatus200AdminTest() throws Exception {
        int pk = 1;

        Users users = givenAdmin();

        Photo photo = givenPhoto();

        Ads ads = givenAds();

        byte[] bytes = "test adsController".getBytes();
        MockMultipartFile mockFile  =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        doReturn(users).when(userService).getUser();

        when(photoRepository.findPhotoByOwner(ads.getTypePhoto(), pk)).thenReturn(Optional.of(photo));

        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH,"/ads/{id}/image", pk)
                        .file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/image/1")));

        fileManager.checkExistFileAndDelete(photo.getFilePath());
    }

    @Test
    @WithMockUser
    @DisplayName("updatePictureAds - обновление фото ads in id file out link status 404")
    public void updatePictureAdsStatus404Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Ads ads = givenAds();

        byte[] bytes = "test adsController".getBytes();
        MockMultipartFile mockFile  =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        doReturn(users).when(userService).getUser();

        when(photoRepository.findPhotoByOwner(ads.getTypePhoto(), pk)).thenReturn(Optional.empty());

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH,"/ads/{id}/image", pk)
                        .file(mockFile))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("updatePictureAds - обновление фото ads in id file out link status 400")
    public void updatePictureAdsStatus400Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Ads ads = givenAds();

        byte[] bytes = "test adsController".getBytes();
        MockMultipartFile mockFile  =
                new MockMultipartFile("image", "image.txt", String.valueOf(MediaType.TEXT_PLAIN), bytes);

        doReturn(users).when(userService).getUser();

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH,"/ads/{id}/image", pk)
                        .file(mockFile))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}