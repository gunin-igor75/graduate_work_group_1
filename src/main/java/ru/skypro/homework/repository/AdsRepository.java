package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.entity.Ads;

import java.util.Collection;


public interface AdsRepository extends JpaRepository<Ads, Integer> {
    @Query(value = "select a.* from ads a  where a.users_id=?1", nativeQuery = true)
    Collection<Ads> findAdsByUserId(Integer id);
}
