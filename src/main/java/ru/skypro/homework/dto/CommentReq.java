package ru.skypro.homework.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentReq {

    private String text;

}
