package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody String currentPassword,
                                         String newPassword) {
        boolean isUpdatePassword = userService.updatePassword(currentPassword, newPassword);
        return isUpdatePassword ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getUser();
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatarUser(@RequestPart(name = "image") MultipartFile image) {
        String fileName = image.getOriginalFilename();
        userService.updateAvatarService(fileName);
        return ResponseEntity.ok().build();
    }
}
