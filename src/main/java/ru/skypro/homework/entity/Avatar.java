package ru.skypro.homework.entity;


import lombok.*;
import javax.persistence.*;
import java.util.Objects;

/**
 * Аватарка
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Avatar extends Photo{

    /** Хозяин аватарки */
    @OneToOne(fetch = FetchType.LAZY)
    private Users users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(super.getId(), avatar.getId());
    }
}
