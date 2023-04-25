package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception_handling.FileNotException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        User user = userService.getUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateAvatarUser(@RequestPart(name = "image") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        if (!checkFile(fileName)) {
            throw new FileNotException();
        }
        userService.updateAvatarService(fileName);
    }

    private boolean checkFile(String fileName) {
        Path path = Paths.get(fileName);
        return Files.exists(path);
    }
}
