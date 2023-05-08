package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.entity.Ads;

import java.util.List;


public interface AdsRepository extends JpaRepository<Ads, Integer> {
    @Query(value = "select a.* from ads a  where a.users_id=?1", nativeQuery = true)
    List<Ads> findAdsByUserId(Integer id);

    List<Ads> findByTitleLike(String title);
}
