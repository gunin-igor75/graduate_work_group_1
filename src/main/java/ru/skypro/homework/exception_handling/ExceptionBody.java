package ru.skypro.homework.exception_handling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionBody {

    private String massage;

    private Map<String, String> errors;

    public ExceptionBody(String massage) {
        this.massage = massage;
    }
}
