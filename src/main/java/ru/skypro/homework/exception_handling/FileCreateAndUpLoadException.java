package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - при создании и скачивании файла
 */
public class FileCreateAndUpLoadException extends RuntimeException {

    public FileCreateAndUpLoadException(String message) {
        super(message);
    }
}
