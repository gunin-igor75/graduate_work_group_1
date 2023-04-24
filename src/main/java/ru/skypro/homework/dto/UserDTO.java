package ru.skypro.homework.dto;

import lombok.*;
import ru.skypro.homework.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String image;

    public static UserDTO from(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .image(user.getImage())
                .build();
    }
}
