package ru.skypro.homework.security_expression;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

/**
 * Класс проверки авторизации пользователя: доступ к объявлениям и коментариям
 */
@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    private final AdsService adsService;

    private final CommentService commentService;

    /**
     * Проверка на право доступа пользователя к объявлению
     * @param adsId - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @return - {@code true} - успех,  {@code false} - неудача
     */
    public boolean canAccessAds(int adsId) {
        Users user = userService.getUser();
        Integer userId = user.getId();
        return adsService.isOwnerAds(adsId, userId) || isAdmin();
    }

    /**
     * Проверка на право доступа пользователя к коментарию
     * @param commentId- индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     * @return - {@code true} - успех,  {@code false} - неудача
     */
    public boolean canAccessComment(int commentId) {
        Users user = userService.getUser();
        Integer userId = user.getId();
        return commentService.isOwnerComment(commentId, userId) || isAdmin();
    }

    /**
     * Проерка роли пользователя
     * @return - {@code true} - ADMIN,  {@code false} - не ADMIN
     */
    private boolean isAdmin() {
        Users user = userService.getUser();
        return user.getRole() == Role.ADMIN;
    }
}
