package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.entity.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query(value = "select p.* from photo p where p.users_id=?1", nativeQuery = true)
    Optional<Photo> findAvatarByUsersId(Integer id);

    @Query(value = "select p.* from photo p where ads_pk=?1", nativeQuery = true)
    Optional<Photo> findPictureByAdsId(Integer id);
}
