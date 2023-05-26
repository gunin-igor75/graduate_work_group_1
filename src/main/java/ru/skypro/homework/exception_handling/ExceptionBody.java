package ru.skypro.homework.exception_handling;

import lombok.Data;

import java.util.Map;

/**
 * Обертка с удобной формы восприятия исключения на фронте
 */
@Data
public class ExceptionBody {

    /** Сообщение исключения*/
    private String massage;

    /**Ключ - поля где нарушена валидация значение -сообщение исключения*/
    private Map<String, String> errors;

    public ExceptionBody(String massage) {
        this.massage = massage;
    }
}
