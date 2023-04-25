package ru.skypro.homework.entity;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String firstName;

    private String lastName;

    private String phone;

    private String image;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static User from(RegisterReq registerReq) {
        return User.builder()
                .userName(registerReq.getUsername())
                .password(new BCryptPasswordEncoder(12).encode(registerReq.getPassword()))
                .firstName(registerReq.getFirstName())
                .lastName(registerReq.getLastName())
                .phone(registerReq.getPhone())
                .build();
    }

}
