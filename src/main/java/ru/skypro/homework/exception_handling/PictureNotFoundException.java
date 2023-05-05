package ru.skypro.homework.exception_handling;

public class PictureNotFoundException extends RuntimeException {
    public PictureNotFoundException() {
    }

    public PictureNotFoundException(String message) {
        super(message);
    }
}
