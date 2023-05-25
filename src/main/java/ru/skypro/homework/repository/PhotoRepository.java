package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.Photo;

import java.util.Optional;

/**
 * Интерфейс для работы с картинками с БД
 */
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    /**
     * Поиск фото по типу фото и id хозяина фото
     * @param type - тип фото Avatar или Picture
     * @param owner_id - идентификатор хозяина фото Users или Ads
     * @return - photo или пусто
     */
    @Query(value = "select p.* from photo p where dtype= :type and (ads_id= :owner_id or users_id= :owner_id)",
            nativeQuery = true)
    Optional<Photo> findPhotoByOwner(@Param("type") String type, @Param("owner_id") Integer owner_id);

}