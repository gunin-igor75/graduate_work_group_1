package ru.skypro.homework.exception_handling;

public class FileNotException extends RuntimeException {
    public FileNotException() {
    }

    public FileNotException(String message) {
        super(message);
    }
}
