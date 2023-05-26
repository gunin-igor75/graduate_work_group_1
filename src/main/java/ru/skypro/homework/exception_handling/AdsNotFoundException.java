package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - отсутствие объявления
 */
public class AdsNotFoundException extends RuntimeException{

    public AdsNotFoundException(String message) {
        super(message);
    }
}
