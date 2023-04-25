package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "abs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private  Integer price;

    private String  title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(id, ads.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
