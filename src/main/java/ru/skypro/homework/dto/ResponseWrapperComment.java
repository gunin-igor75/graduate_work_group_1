package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseWrapperComment {

    private Integer count;

    private List<CommentDTO> results;

}
