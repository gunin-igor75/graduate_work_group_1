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
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.WebSecurityConfig;
import ru.skypro.homework.dto.UserDTO;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.constant.Value.givenUserDTO;
import static ru.skypro.homework.constant.Value.givenUsers;

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

    @MockBean
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
    @DisplayName("setPassword - поменять пароль in - NewPassword  out - status 200")
    public void setPasswordTest() throws Exception {
        String currentPassword = "11111111";
        String newPassword = "55555555";
        JSONObject jsonPassword = new JSONObject();
        jsonPassword.put("currentPassword", currentPassword);
        jsonPassword.put("newPassword", newPassword);

        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(jsonPassword.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(encoder.matches(newPassword, users.getPassword())).isTrue();
    }

    @Test
    @WithMockUser
    @DisplayName("getMe получить юзера  out - UserDTO")
    public void getUserTest() throws Exception {
        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        UserDTO userDTO = givenUserDTO();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.phone").value(userDTO.getPhone()));

    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера - in - UserDTO  out - status 200 UserDto")
    public void updateUserTestStatus200() throws Exception {
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jsonUserDto.get("id")))
                .andExpect(jsonPath("$.email").value(jsonUserDto.get("email")))
                .andExpect(jsonPath("$.firstName").value(jsonUserDto.get("firstName")))
                .andExpect(jsonPath("$.lastName").value(jsonUserDto.get("lastName")))
                .andExpect(jsonPath("$.phone").value(jsonUserDto.get("phone")));

    }

    @Test
    @WithMockUser
    @DisplayName("updateUser обновить юзера - in - UserDTO  out - status 204 UserDto")
    public void updateUserTestStatus204() throws Exception {
        JSONObject jsonUserDto = givenJsonUserDTOWithoutChanges();

        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(jsonUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser
    @DisplayName("updateAvatarUser обновить аватарку in файл картинка out status 200")
    public void updateAvatarUserTest() throws Exception {

        Users users = givenUsers();


        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);


        doNothing().when(userService).createOrUpdateAvatar(any(MultipartFile.class));

        byte[] bytes = "test".getBytes();

        MockMultipartFile file =
                new MockMultipartFile("image", "img.jpg",
                        MediaType.MULTIPART_FORM_DATA_VALUE, bytes);

        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image")
                        .file(file))
                        .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
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

    private JSONObject givenJsonUserDTOWithChanges() throws Exception {
        Integer id = 1;
        String email = "user@mail.ru";
        String firstName = "oleg";
        String lastName = "olegok";
        String phone = "+79139792535";

        JSONObject jsonUserDTO = new JSONObject();
        jsonUserDTO.put("id", id);
        jsonUserDTO.put("email", email);
        jsonUserDTO.put("firstName", firstName);
        jsonUserDTO.put("lastName", lastName);
        jsonUserDTO.put("phone", phone);

        return jsonUserDTO;
    }

    private JSONObject givenJsonUserDTOWithoutChanges() throws Exception {
        Integer id = 1;
        String email = "user@mail.ru";
        String firstName = "igor";
        String lastName = "igoreck";
        String phone = "+79139792520";

        JSONObject jsonUserDTO = new JSONObject();
        jsonUserDTO.put("id", id);
        jsonUserDTO.put("email", email);
        jsonUserDTO.put("firstName", firstName);
        jsonUserDTO.put("lastName", lastName);
        jsonUserDTO.put("phone", phone);

        return jsonUserDTO;
    }
}
























