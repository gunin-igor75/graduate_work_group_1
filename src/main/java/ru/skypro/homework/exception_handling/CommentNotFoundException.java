package ru.skypro.homework.exception_handling;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException() {
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
