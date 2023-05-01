package ru.skypro.homework.exception_handling;

public class AvatarNotFoundException extends RuntimeException {

    public AvatarNotFoundException() {
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }
}
