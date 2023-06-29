package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - отсутствие картинки
 */
public class PhotoNotFoundException extends RuntimeException {

    public PhotoNotFoundException(String message) {
        super(message);
    }
}
