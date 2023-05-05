package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterReq {

    @NotNull(message = "Email must be not null")
    @Length(max = 255, message = "Email has min 0 symbol max 255 Symbol")
    private String username;

    @NotNull(message = "Password must be not null")
    @Length(min = 8, max = 255, message = "Password has min 0 symbol max 255 Symbol")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    @NotNull(message = "FirstName must be not null")
    @Length(min = 2, max = 255, message = "FirstName has min 0 symbol max 255 Symbol")
    private String firstName;

    @NotNull(message = "LastName must be not null")
    @Length(min = 2,max = 255, message = "LastName has min 0 symbol max 255 Symbol")
    private String lastName;

    @NotNull(message = "Phone number must be not null")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

    private Role role;
}







