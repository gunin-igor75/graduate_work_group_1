package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ads implements Comparable<Ads> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String image;

    @Column(nullable = false)
    private Integer price;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String description;

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
}
