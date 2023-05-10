package ru.skypro.homework.security_expression;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    private final AdsService adsService;

    private final CommentService commentService;

    public boolean canAccessAds(int adsId) {
        Users user = userService.getAuthorizedUser();
        Integer userId = user.getId();
        return adsService.isOwnerAds(adsId, userId) || isAdmin();
    }

    public boolean canAccessComment(int commentId) {
        Users user = userService.getAuthorizedUser();
        Integer userId = user.getId();
        return commentService.isOwnerComment(commentId, userId) || isAdmin();
    }

    private boolean isAdmin() {
        Users user = userService.getAuthorizedUser();
        return user.getRole() == Role.ADMIN;
    }
}
