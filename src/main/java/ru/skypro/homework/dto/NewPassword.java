package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Пароль
 */
@Data
@AllArgsConstructor
public class NewPassword {

    /** Теку*щий пароль пользователя */
    private String currentPassword;

    /** Новый пароль пользователя */
    @NotNull(message = "Password must be not null")
    @Length(min = 8, max = 255, message = "Password has min 0 symbol max 255 Symbol")
    private String newPassword;
}
