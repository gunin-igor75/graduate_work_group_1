package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Value.*;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    @SpyBean
    private AuthServiceImpl authService;

    @SpyBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("login - проверка пароля и имени пользователя in LoginReq status 200")
    public void loginStatus200Test() throws Exception {
        String username = "user@email.ru";
        String password = "11111111";

        Users users = givenUsers();

        JSONObject jsonLoginReq = givenJsonLoginReq(username, password);

        when(userService.findUserByEmail(username)).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(jsonLoginReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("login - проверка пароля и имени пользователя in LoginReq status Unauthorised")
    public void loginStatusUnauthorisedTest() throws Exception {
        String username = "user@email.ru";
        String password = "55555555";

        Users users = givenUsers();

        JSONObject jsonLoginReq = givenJsonLoginReq(username, password);

        when(userService.findUserByEmail(username)).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(jsonLoginReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register - регистрация пользователя in RegisterReq status CREATED")
    public void registerStatusCreatedTest() throws Exception {
        JSONObject jsonRegisterReq = givenJsonRegisterReq();

        Users users = givenUsers();

        when(userRepository.save(any(Users.class))).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(jsonRegisterReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("register - регистрация пользователя in RegisterReq status UNAUTHORIZED")
    public void registerStatusUnauthorisedTest() throws Exception {
        JSONObject jsonRegisterReq = givenJsonRegisterReq();

        Users users = givenUsers();

        when(userService.findUserByEmail(users.getEmail())).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(jsonRegisterReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register - регистрация пользователя in RegisterReq status 400")
    public void registerStatus400Test() throws Exception {
        JSONObject jsonRegisterReq = givenJsonRegisterReqBad();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(jsonRegisterReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}