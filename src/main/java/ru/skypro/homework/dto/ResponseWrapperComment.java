package ru.skypro.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ResponseWrapperComment {

    private Integer count;

    private Collection<CommentDTO> results;

}
