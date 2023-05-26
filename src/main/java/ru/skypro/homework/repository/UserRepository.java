package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Users;

import java.util.Optional;

/**
 * Интерфейс для работы с пользователями с БД
 */
public interface UserRepository extends JpaRepository<Users, Integer> {

    /**
     * Поиск пользователя по email
     * @param email - email пользователя
     * @return найденный пользователь или пусто
     */
    Optional<Users> findByEmail(String email);
}
