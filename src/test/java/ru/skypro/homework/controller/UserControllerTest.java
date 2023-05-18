package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.util.FileManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserServiceImpl userService;

    @MockBean
    private PhotoService photoService;

    @MockBean
    private FileManager fileManager;

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("setPassword in - NewPassword  out - status 200")
    void setPasswordTest() throws Exception {
        String currentPassword = "11111111";
        String newPassword = "55555555";
        JSONObject jsPassword =new JSONObject();
        jsPassword.put("currentPassword", currentPassword);
        jsPassword.put("newPassword", newPassword);

        Users users = givenUsers();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users));

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(jsPassword.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(encoder.matches(newPassword, users.getPassword())).isTrue();
    }



    @Test
    @DisplayName("getMe  out - UserDTO")
    void getUserTest() throws Exception {
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
    @DisplayName("updateUser - in - UserDTO  out - status 200 UserDto")
    void updateUserTestStatus200() {
        Users oldUsers = givenUsers();
        UserDTO userDTO = givenUserDTO();
        

    }
    @Test
    @DisplayName("updateUser - in - UserDTO  out - status 204 UserDto")
    void updateUserTestStatus204() {
        Users oldUsers = givenUsers();
        UserDTO userDTO = givenUserDTO();


    }



    private Users givenUsers() {
        return Users.builder()
                .id(1)
                .email("user@mail.ru")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .password("$2a$12$Dn88gLtjUPpzOfajqvrLzu9hwI/nlahRKZm9s9O4wP/n0SgWmU22S")
                .build();
    }

    private UserDTO givenUserDTO() {
        return UserDTO.builder()
                .id(1)
                .email("user@mail.ru")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .build();
    }
}
























