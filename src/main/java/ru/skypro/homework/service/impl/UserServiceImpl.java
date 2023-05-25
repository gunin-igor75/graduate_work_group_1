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

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * Сервис-класс определяющий логику создания, получения, изменения, удаления пользователя в БД
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final UserMapper mapper;

    private final PasswordEncoder encoder;

    @Value("${image.endpoint}")
    private String endpointImage;

    /**
     * Сохранение пользователя в БД
     * @param user - пользователь
     */
    @Override
    public void createUsers(Users user) {
        userRepository.save(user);
    }

    /**
     * Оновление пароля пользователя
     * @param currentPassword - текущий пароль
     * @param newPassword - новый пароль
     * @return - {@code true} - успех,  {@code false} - неудача
     */
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

    /**
     * Обновление параметров пользователя: имя, фамилия, телефон
     * @param userDTO - необходимые параметры для обновления пользователя
     * @return - сущность для отображения пользователя на фронте
     */
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        Users user = getUsersByEmail(email);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        Users saveUser = userRepository.save(user);
        return mapper.userToUserDTO(saveUser);
    }

    /**
     * Создание или обновление аватарки пользователя в БД
     * @param file - файл картинки
     */
    @Override
    @Transactional
    public void createOrUpdateAvatar(MultipartFile file) {
        Users user = getUser();
        if (user.getImage() != null) {
            photoService.updatePhoto(user, file);
        } else {
            Photo photo = photoService.createPhoto(user, file);
            user.setImage(endpointImage + photo.getId());
            userRepository.save(user);
        }
    }

    /**
     * Получение аутентифицированного пользователя с преобразованием из {@code User} в {@code UserDTO}
     * @return - аутентифицированный пользователь
     */
    @Override
    public UserDTO getUserDTO() {
        Users user = getUser();
        return mapper.userToUserDTO(user);
    }

    /**
     * Получение пользователя по email из БД
     * @param email - электронная почта пользователя
     * @return -пользователь либо пусто
     */
    @Override
    public Optional<Users> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Получение аутентифицированного пользователя
     * @return - аутентифицированный пользователь
     */
    @Override
    public Users getUser() {
        String userName = getUserName();
        return getUsersByEmail(userName);
    }

    /**
     * Проверка: изменились ли параметры пользователя
     * @param userDTO - измененные параметры пользователя
     * @return - {@code true} - успех,  {@code false} - неудача
     */
    @Override
    public boolean checkUserUpdate(UserDTO userDTO) {
        Users user = getUser();
        return user.getFirstName().equals(userDTO.getFirstName())
                && user.getLastName().equals(userDTO.getLastName())
                && user.getPhone().equals(userDTO.getPhone());
    }

    /**
     * Получение email пользователя, если он прошел аунтетификацию
     * @return - email пользователя
     * @throws UsernameNotFoundException пользователя нет в {@code Authentication}
     */
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

    /**
     * Получение пользователя по email из БД
     * @param email - email пользователя
     * @return - найденный пользователь
     * @throws UserNotFoundException - щтсутствие пользователя в БД
     */
    private Users getUsersByEmail(String email) {
        Optional<Users> usersOrNull = findUserByEmail(email);
        if (usersOrNull.isEmpty()) {
            String message = "User with email " + email + " is not in the database";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        return usersOrNull.get();
    }
}
