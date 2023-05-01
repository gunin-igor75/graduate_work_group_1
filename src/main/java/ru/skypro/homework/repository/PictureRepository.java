package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Picture;

public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
