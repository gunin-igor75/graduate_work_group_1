package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Ads;

import java.util.Collection;
import java.util.Optional;


public interface AdsRepository extends JpaRepository<Ads, Integer> {
    Optional<Ads> findById (Integer id);
    Collection<Ads> finByAuthorId (Integer authorId);


}
