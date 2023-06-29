package ru.skypro.homework.exception_handling;

/**
 * Кастомное исключение - при удалении файла
 */
public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String message) {
        super(message);
    }

}
