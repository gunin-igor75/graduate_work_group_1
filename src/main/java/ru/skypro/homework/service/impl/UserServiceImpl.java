package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.UserNotFoundException;
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
    public void createOrUpdateAvatar(MultipartFile file) {
        Users user = getUser();
        Path filePath = fileManager.getRandomPath(file, directoryAvatar);
        Photo avatar = photoService.getAvatarByUsersIdOrGetNew(user);
        String currentFileName = avatar.getFilePath();
        fileManager.checkExistFileAndDelete(currentFileName);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        Photo photo = photoService.createOrUpdatePhoto(avatar);
        checkUserAvatarExist(user, photo.getId());
        fileManager.upLoadFile(file, filePath);
    }

    @Override
    public UserDTO getUserDTO() {
        Users user = getUser();
        return mapper.userToUserDTO(user);
    }

    @Override
    public Optional<Users> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users getUser() {
        String userName = getUserName();
        return getUsersByEmail(userName);
    }

    @Override
    public boolean checkUserUpdate(UserDTO userDTO) {
        Users user = getUser();
        return user.getFirstName().equals(userDTO.getFirstName())
                && user.getLastName().equals(userDTO.getLastName())
                && user.getPhone().equals(userDTO.getPhone());
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object obj = authentication.getPrincipal();
        if (obj instanceof UserDetails) {
            UserDetails principal = (UserDetails) obj;
            return principal.getUsername();
        }
        String message = "There is no user in authentication";
        log.error(message);
        throw new UsernameNotFoundException(message);
    }

    private Users getUsersByEmail(String email) {
        Optional<Users> usersOrNull = findUserByEmail(email);
        if (usersOrNull.isEmpty()) {
            String message = "User with email " + email + " is not in the database";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        return usersOrNull.get();
    }

    private Users getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> {
                    String message = "user with id " + id + " is not in the database";
                    log.error(message);
                    return new UserNotFoundException(message);
                }
        );
    }

    private void checkUserAvatarExist(Users user, Integer id) {
        if (user.getImage() == null) {
            user.setImage(endpointImage + id);
            userRepository.save(user);
        }
    }
}
