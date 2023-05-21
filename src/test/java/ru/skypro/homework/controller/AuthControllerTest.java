package ru.skypro.homework.controller;

import org.json.JSONException;
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
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.constant.Value.givenUsers;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @SpyBean
    private AuthServiceImpl authService;

    @Autowired
    private  PasswordEncoder encoder;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private UserMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("login - проверка пароля и имени пользователя status 200 in LoginReq ")
    public void loginStatusOkTest() throws Exception{
        String  username = "user@email.ru";
        String password = "11111111";

        Users users = givenUsers();

        JSONObject jsonLoginReq = givenJsonLoginReq(username, password);

        when(userService.findUserByEmail(username)).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .content(jsonLoginReq.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("login - проверка пароля и имени пользователя status Unauthorised in LoginReq ")
    public void loginStatusUnauthorisedTest() throws Exception{
        String  username = "user@email.ru";
        String password = "55555555";

        Users users = givenUsers();

        JSONObject jsonLoginReq = givenJsonLoginReq(username, password);

        when(userService.findUserByEmail(username)).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(jsonLoginReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register - регистрация пользователя status CREATED in RegisterReq")
    public void registerStatusCreatedTest() throws Exception {
        JSONObject jsonRegisterReq = givenJsonRegisterReq();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(jsonRegisterReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("register - регистрация пользователя status UNAUTHORIZED in RegisterReq")
    public void registerStatusUnauthorisedTest() throws Exception {
        JSONObject jsonRegisterReq = givenJsonRegisterReq();

        Users users = givenUsers();

        when(userService.findUserByEmail(users.getEmail())).thenReturn(Optional.of(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(jsonRegisterReq.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private JSONObject givenJsonLoginReq(String username, String password) throws JSONException {
        JSONObject jsonLoginReq = new JSONObject();
        jsonLoginReq.put("username", username);
        jsonLoginReq.put("password", password);
        return jsonLoginReq;
    }

    private JSONObject givenJsonRegisterReq() throws JSONException {
        String username = "user@mail.ru";
        String firstName = "igor";
        String lastname = "igoreck";
        String phone = "+79139792520";
        String password = "11111111";

        JSONObject jsonRegisterReq = new JSONObject();
        jsonRegisterReq.put("username", username);
        jsonRegisterReq.put("firstName", firstName);
        jsonRegisterReq.put("lastName", lastname);
        jsonRegisterReq.put("phone", phone);
        jsonRegisterReq.put("password", password);
        return jsonRegisterReq;
    }
}