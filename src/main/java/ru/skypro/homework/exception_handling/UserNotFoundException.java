package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - отсутствие пользователя
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
    }
}
