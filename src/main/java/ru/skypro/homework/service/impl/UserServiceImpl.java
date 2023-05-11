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
import java.util.Optional;


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
    private String directoryAvatar;

    @Value("${image.endpoint}")
    private String endpointImage;


    @Override
    public Users findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                UserNotFoundException::new
        );
    }

    @Override
    public void createOrUpdateUsers(Users user) {
        if (user == null) {
            throw  new UserNotFoundException();
        }
        userRepository.save(user);
    }

    @Override
    public Users getUsersByEmail(String email) {
        Optional<Users> usersOrNull = userRepository.findByEmail(email);
        if (usersOrNull.isEmpty()) {
            throw new UserNotFoundException();
        }
        return usersOrNull.get();
    }

    @Override
    public boolean isRegistrationrUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        Users user = getAuthorizedUser();
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
        Users persistentUser = findUserById(id);
        Users users = mapper.userDTOToUsers(userDTO);
        users.setPassword(persistentUser.getPassword());
        users.setRole(persistentUser.getRole());
        Users saveUser = userRepository.save(users);
        return mapper.userToUserDTO(saveUser);
    }

    @Override
    @Transactional
    public UserDTO createOrUpdateAvatar(MultipartFile file) {
        Users authorizedUser = getAuthorizedUser();
        Users user = userRepository.findById(authorizedUser.getId()).orElseThrow();
        Path filePath = fileManager.getRandomPath(file, directoryAvatar);
        Photo avatar =  photoService.getAvatarByUsersIdOrGetNew(user);
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
    public UserDTO getUser() {
        Users authorizedUser = getAuthorizedUser();
        return mapper.userToUserDTO(authorizedUser);
    }


    @Override
    public Users getAuthorizedUser() {
        String userName = getAuthorizedUserName();
        return userRepository.findByEmail(userName).orElseThrow(
                UserNotFoundException::new
        );
    }

    private Users checkUserAvatarExist(Users user, Integer id) {
        if (user.getImage() == null) {
            user.setImage(endpointImage + id);
            return userRepository.save(user);
        }
        return user;
    }

    private String getAuthorizedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users principal = (Users) authentication.getPrincipal();
        return principal.getUsername();
    }
}
