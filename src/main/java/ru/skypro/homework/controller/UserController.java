package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileManager;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Validated
public class UserController {

    private final UserService userService;

    private final FileManager fileManager;

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@Valid @RequestBody NewPassword pairPassword) {
        boolean isUpdatePassword = userService.updatePassword(pairPassword.getCurrentPassword(),
                pairPassword.getNewPassword());
        return isUpdatePassword ? ResponseEntity.ok().build() : ResponseEntity.status(NOT_FOUND).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getUserDTO();
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        if (userService.checkUserUpdate(userDTO)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        UserDTO newUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(newUserDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatarUser(@RequestPart(name = "image") MultipartFile file) {
        fileManager.checkFile(file);
        userService.createOrUpdateAvatar(file);
        return ResponseEntity.ok().build();
    }
}
