package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UserDTO {

    @NotNull(message = "Id must be not null")
    private Integer id;

    @NotNull(message = "Email must be not null")
    private String email;

    @NotNull(message = "FirstName must be not null")
    @Length(min = 2, max = 255, message = "FirstName Max 255 Symbol")
    private String firstName;

    @NotNull(message = "LastName must be not null")
    @Length(min = 2, max = 255, message = "LastName Max 255 Symbol")
    private String lastName;

    @NotNull(message = "tName must be not null")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

    private String image;
}
