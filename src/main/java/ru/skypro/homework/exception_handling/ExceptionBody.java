package ru.skypro.homework.exception_handling;

import lombok.Data;

import java.util.Map;

@Data
public class ExceptionBody {

    private String massage;

    private Map<String, String> errors;

    public ExceptionBody(String massage) {
        this.massage = massage;
    }
}
