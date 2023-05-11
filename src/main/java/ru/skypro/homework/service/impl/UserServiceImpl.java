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
import ru.skypro.homework.entity.Avatar;
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


@Service("userServiceImp")
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PhotoService photoService;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    private final FileManager fileManager;

    @Value("${users.avatar.dir.path}")
    private String DIRECTORY_AVATAR;

    @Value("${image.endpoint}")
    private String ENDPOINT_IMAGE;


    @Override
    public Users findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
    }

    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        Users authorizedUser = getAuthorizedUser();
        boolean isPasswordGood = encoder.matches(currentPassword, authorizedUser.getPassword());
        if (isPasswordGood) {
            authorizedUser.setPassword(encoder.encode(newPassword));
            userRepository.save(authorizedUser);
            return true;
        }
        return false;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Integer id = userDTO.getId();
        Users persistentUser = findUserById(id);
        Users users = mapper.userDTOToUsers(userDTO);
        users.setPassword(persistentUser.getPassword());
        users.setRole(persistentUser.getRole());
        Users saveUser = userRepository.save(users);
        return mapper.userToUserDTO(saveUser);
    }

    @Override
    @Transactional
    public void createOrUpdateAvatar(MultipartFile file) {
        Users user = getAuthorizedUser();
        Integer id = user.getId();
        Path filePath = fileManager.getRandomPath(file, DIRECTORY_AVATAR);
        fileManager.upLoadFile(file, filePath);
        Avatar avatar = (Avatar) photoService.getCurrentAvatarOrNew(id);
        String currentFileName = avatar.getFilePath();
        fileManager.checkExistFileAndDelete(currentFileName);
        avatar.setUsers(user);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        Photo photo = photoService.createPhoto(avatar);
        checkUserAvatarExist(user, photo.getId());
    }


    @Override
    public UserDTO getUser() {
        Users authorizedUser = getAuthorizedUser();
        return mapper.userToUserDTO(authorizedUser);
    }


    @Override
    public Users getAuthorizedUser() {
        String userName = getAuthorizedUserName();
        return userRepository.findByEmail(userName).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
    }

    private void checkUserAvatarExist(Users user, Integer id) {
        if (user.getImage() == null) {
            user.setImage(ENDPOINT_IMAGE + id);
            userRepository.save(user);
        }
    }

    private String getAuthorizedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users principal = (Users) authentication.getPrincipal();
        return principal.getUsername();
    }
}
