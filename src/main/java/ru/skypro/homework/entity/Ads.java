package ru.skypro.homework.entity;

import lombok.*;
import ru.skypro.homework.service.OwnerPhoto;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Объявление
 */
@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ads implements Comparable<Ads>, OwnerPhoto {

    /** Индентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Эндпоинт */
    private String image;

    /** Стоимость */
    @Column(nullable = false)
    private Integer price;

    /** Название */
    @Column(unique = true, nullable = false)
    private String title;

    /** Щписание */
    @Column(unique = true, nullable = false)
    private String description;

    /** Хозяин объявления */
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(
            mappedBy = "ads",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

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

    @Override
    public int compareTo(Ads o) {
        return this.title.compareTo(o.title);
    }

    @Override
    public String getTypePhoto() {
        return "Picture";
    }
}
