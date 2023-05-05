package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class NewPassword {

    private String currentPassword;

    @NotNull(message = "Password must be not null")
    @Length(min = 8, max = 255, message = "Password has min 0 symbol max 255 Symbol")
    private String newPassword;
}
