package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с коментариями с БД
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Поиск комментариев по id щбъявления
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @return - коллекция коментариев
     */
    List<Comment> findByAds_Id(int id);

    /**
     * Поиск коментариев по id и id пользователя
     * @param commentId - индентификатор по каторому коментарий хранится в БД не может быть {@code null}
     * @param usersId - индентификатор по каторому пользователь хранится в БД не может быть {@code null}
     * @return - найденый коментарий
     */
    Optional<Comment> findCommentByIdAndUsersId(int commentId, int usersId);

}
