package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Фото родительский класс для Avatar и Picture
 */
@Entity
@Table(name = "photo")
@Inheritance
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Путь к файлу */
    @Column(nullable = false)
    private String filePath;

    /** mediaType файла*/
    @Column(nullable = false)
    private String mediaType;

    /*** Размер файла */
    @Column(nullable = false)
    private long fileSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
