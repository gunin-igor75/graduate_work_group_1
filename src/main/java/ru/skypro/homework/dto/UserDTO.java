package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Пользователь
 */
@Data
@Builder
public class UserDTO {

    /** Идентификатор пользователя*/
    @NotNull(message = "Id must be not null")
    private Integer id;

    /** Почта пользователя */
    @NotNull(message = "Email must be not null")
    private String email;

    /** Имя пользователя */
    @NotNull(message = "FirstName must be not null")
    @Length(min = 2, max = 255, message = "FirstName Max 255 Symbol")
    private String firstName;

    /** Фамилия поьзователя */
    @NotNull(message = "LastName must be not null")
    @Length(min = 2, max = 255, message = "LastName Max 255 Symbol")
    private String lastName;

    /** Номер телефона пользователя */
    @NotNull(message = "tName must be not null")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

    /** Эндпоинт аватарки пользователя*/
    private String image;
}
