package ru.skypro.homework.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Picture extends Photo{

    @OneToOne(fetch = FetchType.LAZY)
    private Ads ads;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(super.getId(), picture.getId());
    }
}
