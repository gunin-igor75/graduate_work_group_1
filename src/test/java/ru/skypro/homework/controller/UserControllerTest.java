package ru.skypro.homework.controller;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.WebSecurityConfig;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.PhotoServiceImp;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.util.FileManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Value.*;

@SpringBootTest
@Import(WebSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserServiceImpl userService;

    @SpyBean
    private PhotoServiceImp photoService;

    @SpyBean
    private FileManager fileManager;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private PhotoRepository photoRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("setPassword - поменять пароль in - NewPassword  out status 200")
    public void setPasswordStatus200Test() throws Exception {
        String currentPassword = "11111111";
        String newPassword = "55555555";

        JSONObject jsonPassword = givenNewPassword(currentPassword, newPassword);

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(jsonPassword.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(encoder.matches(newPassword, users.getPassword())).isTrue();
    }

    @Test
    @WithMockUser
    @DisplayName("setPassword - поменять пароль in - NewPassword  out status 404")
    public void setPasswordStatus404Test() throws Exception {
        String currentPassword = "11111115";
        String newPassword = "55555555";

        JSONObject jsonPassword = givenNewPassword(currentPassword, newPassword);

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(jsonPassword.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("setPassword - поменять пароль in - NewPassword  out status 400")
    public void setPasswordStatus400Test() throws Exception {
        String currentPassword = "11111111";
        String newPassword =  givenRandomString(256);

        JSONObject jsonPassword = givenNewPassword(currentPassword, newPassword);

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(jsonPassword.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("getMe получить юзера out - UserDTO status 200")
    public void getUserStatus200Test() throws Exception {
        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        UserDTO userDTO = givenUserDTO();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.phone").value(userDTO.getPhone()));
    }

    @Test
    @WithMockUser
    @DisplayName("getMe получить юзера  out - status 404")
    public void getUserStatus404Test() throws Exception {

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера  in - UserDTO  out - status 200 UserDto")
    public void updateUserStatus200Test() throws Exception {
        JSONObject jsonUserDto = givenJsonUserDTOWithChanges();

        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(jsonUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jsonUserDto.get("id")))
                .andExpect(jsonPath("$.email").value(jsonUserDto.get("email")))
                .andExpect(jsonPath("$.firstName").value(jsonUserDto.get("firstName")))
                .andExpect(jsonPath("$.lastName").value(jsonUserDto.get("lastName")))
                .andExpect(jsonPath("$.phone").value(jsonUserDto.get("phone")));
    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера  in - UserDTO  out - status 204")
    public void updateUserTestStatus204() throws Exception {
        JSONObject jsonUserDto = givenJsonUserDTOWithoutChanges();

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(jsonUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера  in - UserDTO  out - status 404")
    public void updateUserTestStatus404() throws Exception {
        JSONObject jsonUserDto = givenJsonUserDTOWithChanges();

        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(jsonUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера  in - UserDTO  out - status 400")
    public void updateUserTestStatus400() throws Exception {
        JSONObject jsonUserDto = givenJsonUserDTOBad();

        Users users = givenUsers();

        doReturn(users).when(userService).getUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(jsonUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("updateAvatarUser обновить аватарку in файл картинка out status 200")
    public void updateAvatarUserTest() throws Exception {

        Users users = givenUsers();

        Photo photo = givenPhoto();

        doReturn(users).when(userService).getUser();

        when(photoRepository.findAvatarByUsersId(users.getId())).thenReturn(Optional.of(photo));

        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        byte[] bytes = "test".getBytes();

        MockMultipartFile file =
                new MockMultipartFile("image", "img.jpg",
                        MediaType.IMAGE_JPEG.toString(), bytes);

        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk());

        fileManager.checkExistFileAndDelete(photo.getFilePath());

    }

    @Test
    @WithAnonymousUser
    @DisplayName("AnonymousUserTest - проверка вход анонимного юзера")
    public void AnonymousUserTest() throws Exception {
        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }
}
























