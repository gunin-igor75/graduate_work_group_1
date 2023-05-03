package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    private final UserMapper mapper;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper mapper) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean login(String userName, String password) {
        Optional<Users> optionalUser = userRepository.findByEmail(userName);
        if (optionalUser.isEmpty()) {
            return false;
        }
        Users users = optionalUser.get();
        try {
            return encoder.matches(password, users.getPassword());
        } catch (UsernameNotFoundException e) {
            log.warn("Wrong username or password");
        }
        return false;
    }

    @Override
    public boolean register(RegisterReq registerReq) {
        Optional<Users> optionalUser = userRepository.findByEmail(registerReq.getUsername());
        if (optionalUser.isPresent()) {
            return false;
        }
        Users users = mapper.registerReqToUsers(registerReq);
        users.setPassword(encoder.encode(registerReq.getPassword()));
        userRepository.save(users);
        return true;
    }
}
