package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;


@Service("userServiceImp")
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDTO getUser() {
        Users authorizedUser = getAuthorizedUser();
        return mapper.userToUserDTO(authorizedUser);
    }

    @Override
    public void updateAvatarService(String fileName) {
        Users authorizedUser = getAuthorizedUser();
        authorizedUser.setImage(fileName);
        userRepository.save(authorizedUser);
    }

    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        return false;
    }

    private Users getAuthorizedUser() {
        String userName = getAuthorizedUserName();
        return userRepository.findByEmail(userName).orElseThrow(
                () -> {
                    throw  new UserNotFoundException();
                }
        );
    }

    private String getAuthorizedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users principal =(Users) authentication.getPrincipal();
        return principal.getUsername();
    }
}
