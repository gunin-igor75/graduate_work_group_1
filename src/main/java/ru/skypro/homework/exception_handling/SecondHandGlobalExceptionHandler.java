package ru.skypro.homework.exception_handling;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class SecondHandGlobalExceptionHandler {

//    private static final Logger log = LoggerFactory.getLogger(SecondHandGlobalExceptionHandler.class);
//
//    @ExceptionHandler
//    public ResponseEntity<HogwartsIncorrectData> handlerException(
//            NoSuchFacultyException exception) {
//        HogwartsIncorrectData data = new HogwartsIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<HogwartsIncorrectData> handlerException(
//            NoSuchStudentException exception) {
//        HogwartsIncorrectData data = new HogwartsIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<HogwartsIncorrectData> handlerException(
//            NoSuchAvatarException exception) {
//        HogwartsIncorrectData data = new HogwartsIncorrectData();
//        data.setInfo(exception.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
//    }
//
//
//    @ExceptionHandler
//    public ResponseEntity<HogwartsIncorrectData> handlerException(
//            Exception exception) {
//        HogwartsIncorrectData data = new HogwartsIncorrectData();
//        data.setInfo(exception.getMessage());
//        log.error("Error {}", exception.toString());
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }
}
