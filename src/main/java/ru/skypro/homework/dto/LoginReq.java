package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Логин пользователя
 */
@Data
public class LoginReq {

    /** Пароль пользователя */
    private String password;

    /** Почта пользователя */
    private String username;
}

