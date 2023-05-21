package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.WebSecurityConfig;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.constant.Value.*;

@SpringBootTest
@Import(WebSecurityConfig.class)
class AdsControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

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


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("getAllAds - вывести все объявления out ResponseWrapperAds")
    public void getAllAdsTest() throws Exception {
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
    @DisplayName("createAds - создание объявления status201 in file CreateAds out AdsDTO")
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

        doReturn(users).when(userService).getUser();

        when(adsRepository.save(any(Ads.class))).thenReturn(ads);

        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

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
    }

    @Test
    @WithMockUser
    @DisplayName("getAds - получить объявление status 200 out FullAds")
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
    @DisplayName("deleteAds удаление объявления in id out No CONTENT")
    public void deleteAdsTesStatusNoContent() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Ads ads = givenAds();

        Photo photo = givenPhoto();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(photoRepository.findById(pk)).thenReturn(Optional.of(photo));

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/ads/{id}", pk))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("updateAds - обновление ads status 200 in CreateAds out AdsDTO")
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
    @DisplayName("getAdsMe - показать ads зарегестрированного user status 200 out ResponseWrapperAds")
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
    @DisplayName("updatePictureAds - обновление фото ads status 200 in id file out link")
    public void updatePictureAdsStatus200Test() throws Exception {
        int pk = 1;

        Users users = givenUsers();

        Photo photo = givenPhoto();

        Ads ads = givenAds();

        byte[] bytes = "test adsController".getBytes();
        MockMultipartFile mockFile  =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        doReturn(users).when(userService).getUser();

        when(photoRepository.findPictureByAdsId(pk)).thenReturn(Optional.of(photo));

        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        when(adsRepository.findById(pk)).thenReturn(Optional.of(ads));

        when(adsRepository.findAdsByIdAndUsersId(pk, users.getId())).thenReturn(Optional.of(ads));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH,"/ads/{id}/image", pk)
                        .file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/image/1")));
    }

    private JSONObject givenJsonCreateAds() throws JSONException {
        String title = "add title ads";
        String description  = "update ads test";
        Integer price = 1000;
        JSONObject jsonCreateAds = new JSONObject();
        jsonCreateAds.put("title", title);
        jsonCreateAds.put("description", description);
        jsonCreateAds.put("price", price);
        return jsonCreateAds;
    }

    private ResponseWrapperAds givenResponseWrapperAds(List<AdsDTO> adsDTO) {
        return ResponseWrapperAds.builder()
                .count(adsDTO.size())
                .results(adsDTO)
                .build();
    }

}