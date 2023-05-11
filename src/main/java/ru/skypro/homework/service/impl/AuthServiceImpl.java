package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;

    private final UserService userService;

    private final UserMapper mapper;


    @Override
    public boolean login(String userName, String password) {
        boolean isRegistration = userService.isRegistrationrUser(userName);
        if (!isRegistration) {
            return false;
        }
        Users user = userService.getUsersByEmail(userName);
        return encoder.matches(password, user.getPassword());
    }

    @Override
    public boolean register(RegisterReq registerReq) {
        boolean isRegistration = userService.isRegistrationrUser(registerReq.getUsername());
        if (isRegistration) {
            return false;
        }
        Users user = mapper.registerReqToUsers(registerReq);
        user.setPassword(encoder.encode(registerReq.getPassword()));
        userService.createOrUpdateUsers(user);
        return true;
    }
}
