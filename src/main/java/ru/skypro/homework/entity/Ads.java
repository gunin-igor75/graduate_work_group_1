package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    private String image;

    private  Integer price;

    private String  title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(pk, ads.pk) && Objects.equals(image, ads.image) && Objects.equals(price, ads.price) && Objects.equals(title, ads.title) && Objects.equals(description, ads.description) && Objects.equals(users, ads.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, image, price, title, description, users);
    }
}
