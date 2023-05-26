package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Регистрационные данные пользователя
 */
@Data
public class RegisterReq {

    /** Почта пользователя*/
    @NotNull(message = "Email must be not null")
    @Length(min = 8, max = 255, message = "Email has min 0 symbol max 255 Symbol")
    private String username;

    /** Пароль пользователя */
    @NotNull(message = "Password must be not null")
    @Length(min = 8, max = 255, message = "Password has min 0 symbol max 255 Symbol")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    /** Имя пользователя */
    @NotNull(message = "FirstName must be not null")
    @Length(min = 2, max = 255, message = "FirstName has min 0 symbol max 255 Symbol")
    private String firstName;

    /** Фамилия пользователя */
    @NotNull(message = "LastName must be not null")
    @Length(min = 2,max = 255, message = "LastName has min 0 symbol max 255 Symbol")
    private String lastName;

    /** Номер телефона пользователя */
    @NotNull(message = "Phone number must be not null")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

    /** Роль пользователя */
    private Role role;
}







