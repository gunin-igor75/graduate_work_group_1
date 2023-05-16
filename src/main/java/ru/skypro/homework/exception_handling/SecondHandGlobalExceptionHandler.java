package ru.skypro.homework.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SecondHandGlobalExceptionHandler {
    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<ExceptionBody> handlerFileDeleteNotFound(FileDeleteException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handlerException(Exception e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ExceptionBody> handlerResourceException(ResourceException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }

    @ExceptionHandler(FileCreateAndUpLoadException.class)
    public ResponseEntity<ExceptionBody> handlerFileCreateAndUpLoad(FileCreateAndUpLoadException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<ExceptionBody> handlerAdsException(AdsNotFoundException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionBody);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionBody> handlerCommentException(CommentNotFoundException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionBody);
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<ExceptionBody> handlerPhotoException(PhotoNotFoundException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleConstraintViolation(ConstraintViolationException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }
}
