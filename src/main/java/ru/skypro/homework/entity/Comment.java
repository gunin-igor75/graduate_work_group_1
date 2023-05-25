package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

/***
 * Коментарий
 */
@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment implements Comparable<Comment>{

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Дата создания коментария*/
    @Column(unique = true, nullable = false)
    private Instant createdAt;

    /** Текст коментария */
    @Column(nullable = false)
    private String text;

    /** Объявление коментария */
    @ManyToOne(fetch = FetchType.LAZY)
    private Ads ads;

    /** Хозяин коментария */
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Comment o) {
        return createdAt.compareTo(o.createdAt);
    }
}
