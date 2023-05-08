package ru.skypro.homework.exception_handling;

public class AdsNotFoundException extends RuntimeException{
    public AdsNotFoundException() {
    }

    public AdsNotFoundException(String message) {
        super(message);
    }
}
