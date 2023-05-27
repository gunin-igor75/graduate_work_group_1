package ru.skypro.homework.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Глобальная обработка исключении в приложении
 */
@RestControllerAdvice
public class SecondHandGlobalExceptionHandler {

    /**
     * Перехватчик исключения {@code FileDeleteException}
     *
     * @param e - исключение при удалении файла
     * @return - обертка с удобной формы восприятия исключения на фронте
     */
    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<ExceptionBody> handlerFileDeleteNotFound(FileDeleteException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }

    /**
     * Перехватчик исключения {@code FileCreateAndUpLoadException}
     *
     * @param e - исключение при создании и скачивании файла
     * @return - обертка с удобной формы восприятия исключения на фронте
     */
    @ExceptionHandler(FileCreateAndUpLoadException.class)
    public ResponseEntity<ExceptionBody> handlerFileCreateAndUpLoad(FileCreateAndUpLoadException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    /**
     * Перехватчик исключения {@code UserNotFoundException}, {@code AdsNotFoundException}
     * {@code CommentNotFoundException}, {@code PhotoNotFoundException}
     * @param e - исключение при отсутствие пользователя, объявления, коментарияб картинки
     * @return - обертка с удобной формы восприятия исключения на фронте
     */
    @ExceptionHandler({UserNotFoundException.class,
                    AdsNotFoundException.class,
                    CommentNotFoundException.class,
                    PhotoNotFoundException.class})
    public ResponseEntity<ExceptionBody> handlerUserNotFound(RuntimeException e) {
        ExceptionBody exceptionBody = new ExceptionBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionBody);
    }

    /**
     * Перехватчик исключения {@code MethodArgumentNotValidException}
     *
     * @param e - исключение при нарушении условий валидации входных данных
     * @return - обертка с удобной формы восприятия исключения на фронте
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }
}
