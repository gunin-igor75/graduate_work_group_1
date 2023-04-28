package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody Map<String, String> map) {
        String currentPassword = map.get("currentPassword");
        String newPassword = map.get("newPassword");
        boolean isUpdatePassword = userService.updatePassword(currentPassword, newPassword);
        return isUpdatePassword ? ResponseEntity.ok().build()
                : ResponseEntity.status(NOT_FOUND).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getUser();
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody RegisterReq registerReq) {
        UserDTO newUserDTO = userService.updateUser(registerReq);
        return ResponseEntity.ok(newUserDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAvatarUser(@RequestPart(name = "image") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        userService.updateAvatarService(fileName);
        return ResponseEntity.ok(fileName);
    }
}