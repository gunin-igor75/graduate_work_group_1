package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.entity.Ads;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с объявлениями с БД
 */
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    /**
     * Поиск объявлений по id gjkmpjdfntkz
     * @param id - индентификатор по каторому пользователь хранится в БД не может быть {@code null}
     * @return - коллекция объявлений
     */
    @Query(value = "select a.* from ads a  where a.users_id=?1", nativeQuery = true)
    List<Ads> findAdsByUserId(Integer id);

    /**
     * Поиск объявления по id  и по id пользователя
     * @param adsId -индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @param usersId - индентификатор по каторому пользователь хранится в БД не может быть {@code null}
     * @return - найденное объявление или пусто
     */
    Optional<Ads> findAdsByIdAndUsersId(int adsId, Integer usersId);
}
