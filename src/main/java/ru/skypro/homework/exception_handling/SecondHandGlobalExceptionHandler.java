package ru.skypro.homework.exception_handling;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class SecondHandGlobalExceptionHandler {

//    private static final Logger log = LoggerFactory.getLogger(SecondHandGlobalExceptionHandler.class);
//
//    @ExceptionHandler
//    public ResponseEntity<SecondHandIncorrectData> handlerException(
//            NoSuchFacultyException exception) {
//        SecondHandIncorrectData data = new SecondHandIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<SecondHandIncorrectData> handlerException(
//            NoSuchStudentException exception) {
//        SecondHandIncorrectData data = new SecondHandIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<SecondHandIncorrectData> handlerException(
//            NoSuchAvatarException exception) {
//        SecondHandIncorrectData data = new SecondHandIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//
//    @ExceptionHandler
//    public ResponseEntity<SecondHandIncorrectData> handlerException(
//            Exception exception) {
//        SecondHandIncorrectData data = new SecondHandIncorrectData();
//        data.setInfo(exception.getMessage());
//        log.error("Error {}", exception.toString());
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }
}
