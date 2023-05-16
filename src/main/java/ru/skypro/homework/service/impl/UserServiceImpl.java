package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.ResourceException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileManager;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final UserMapper mapper;

    private final PasswordEncoder encoder;

    private final FileManager fileManager;

    @Value("${users.avatar.dir.path}")
    private String directoryAvatar;

    @Value("${image.endpoint}")
    private String endpointImage;


    @Override
    public void createUsers(Users user) {
        userRepository.save(user);
    }

    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        Users user = getUser();
        boolean isPasswordGood = encoder.matches(currentPassword, user.getPassword());
        if (isPasswordGood) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Integer id = userDTO.getId();
        Users user = getUserById(id);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        Users saveUser = userRepository.save(user);
        return mapper.userToUserDTO(saveUser);
    }

    @Override
    @Transactional
    public UserDTO createOrUpdateAvatar(MultipartFile file) {
        Users user = getUser();
        Path filePath = fileManager.getRandomPath(file, directoryAvatar);
        Photo avatar = photoService.getAvatarByUsersIdOrGetNew(user);
        String currentFileName = avatar.getFilePath();
        fileManager.checkExistFileAndDelete(currentFileName);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        Photo photo = photoService.createOrUpdatePhoto(avatar);
        Users newUser = checkUserAvatarExist(user, photo.getId());
        fileManager.upLoadFile(file, filePath);
        return mapper.userToUserDTO(newUser);
    }

    @Override
    public UserDTO getUserDTO() {
        Users authorizedUser = getUser();
        return mapper.userToUserDTO(authorizedUser);
    }

    @Override
    public Optional<Users> getRegistrationUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users getUser() {
        String userName = getUserName();
        return getUsersByEmail(userName);
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof Users) {
            Users principal = (Users) authentication.getPrincipal();
            return principal.getUsername();
        }
        String message = "There is no user in authentication";
        log.error(message);
        throw new ResourceException(message);
    }

    private Users getUsersByEmail(String email) {
        Optional<Users> usersOrNull = getRegistrationUser(email);
        if (usersOrNull.isEmpty()) {
            String message = "The authenticated user is not in the database";
            log.error(message);
            throw new ResourceException(message);
        }
        return usersOrNull.get();
    }

    private Users getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> {
                    String message = "The authenticated user is not in the database";
                    log.error(message);
                    return new ResourceException(message);
                }
        );
    }

    private Users checkUserAvatarExist(Users user, Integer id) {
        if (user.getImage() == null) {
            user.setImage(endpointImage + id);
            return userRepository.save(user);
        }
        return user;
    }
}
