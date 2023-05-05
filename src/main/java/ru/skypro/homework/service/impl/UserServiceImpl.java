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
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.FileCreateAndUpLoadException;
import ru.skypro.homework.exception_handling.FileNotException;
import ru.skypro.homework.exception_handling.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service("userServiceImp")
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AvatarService avatarService;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Value("${users.avatar.dir.path}")
    private String DIRECTORY_AVATAR;

    @Value("${users.image.endpoint}")
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
    public void updateAvatarService(MultipartFile file) {
        Users user = getAuthorizedUser();
        Integer id = user.getId();
        Path filePath = Path.of(DIRECTORY_AVATAR, id + "." +
                getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        upLoadFile(file, filePath);
        Avatar avatar = avatarService.getCurrentAvatarOrNew(id);
        checkExistFileAndDelete(avatar);
        avatar.setUsers(user);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatarService.avatarCreateOrUpdate(avatar);
        checkUserAvatarExist(user);
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

    private void checkExistFileAndDelete(Avatar avatar) {
        String filePath = avatar.getFilePath();
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new FileNotException();
            }
        }
    }

    private void upLoadFile(MultipartFile file, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (InputStream in = file.getInputStream();
                 OutputStream out = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(in, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(out, 1024)
            ) {
                bis.transferTo(bos);
            }
        } catch (IOException e) {
            log.error("Error create or upload file");
            throw new FileCreateAndUpLoadException();
        }
    }

    private void checkUserAvatarExist(Users user) {
        if (user.getImage() == null) {
            user.setImage(ENDPOINT_IMAGE + user.getId());
            userRepository.save(user);
        }
    }

    private String getAuthorizedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users principal = (Users) authentication.getPrincipal();
        return principal.getUsername();
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
