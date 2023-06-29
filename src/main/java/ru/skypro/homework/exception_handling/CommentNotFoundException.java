package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - отсутствие коментария
 */
public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String message) {
        super(message);
    }
}
