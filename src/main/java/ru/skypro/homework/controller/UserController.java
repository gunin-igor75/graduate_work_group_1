package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.exception_handling.FileNotException;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.UserService;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    private final AvatarService avatarService;
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword pairPassword) {
        boolean isUpdatePassword = userService.updatePassword(pairPassword.getCurrentPassword(),
                pairPassword.getNewPassword());
        return isUpdatePassword ? ResponseEntity.ok().build()
                : ResponseEntity.status(NOT_FOUND).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getUser();
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO newUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(newUserDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatarUser(@RequestPart(name = "image") MultipartFile image) {
        if (image.getSize() == 0) {
            return ResponseEntity.badRequest().build();
        }
        userService.updateAvatarService(image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/image/{id}")
    public void downLoadAvatar(@PathVariable("id") Integer id,
                               HttpServletResponse response) {
        Avatar avatar = avatarService.getAvatarById(id);
        Path path = Paths.get(avatar.getFilePath());
        try (InputStream in = Files.newInputStream(path);
             OutputStream out = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            in.transferTo(out);
        } catch (IOException e) {
            throw new FileNotException();
        }
    }
}
