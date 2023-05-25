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

import java.util.Optional;

/**
 * Аутентификация и регистрация пользователей
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;

    private final UserService userService;

    private final UserMapper mapper;

    /**
     * Проверка логина и пароля
     * @param userName - email пользователя
     * @param password - текущий пароль
     * @return -{@code true} - успех, {@code false} - провал
     */
    @Override
    public boolean login(String userName, String password) {
        Optional<Users> usersOrEmpty = userService.findUserByEmail(userName);
        if (usersOrEmpty.isEmpty()) {
            return false;
        }
        Users user = usersOrEmpty.get();
        return encoder.matches(password, user.getPassword());
    }

    /**
     * Регистрация пользователя
     * @param registerReq - сущность, содержащая все необходимые данные для регистраии
     * @return - {@code true} - успех, {@code false} - провал
     */
    @Override
    public boolean register(RegisterReq registerReq) {
        Optional<Users> usersOrEmpty = userService.findUserByEmail(registerReq.getUsername());
        if (usersOrEmpty.isPresent()) {
            return false;
        }
        Users user = mapper.registerReqToUsers(registerReq);
        user.setPassword(encoder.encode(registerReq.getPassword()));
        userService.createUsers(user);
        return true;
    }
}
