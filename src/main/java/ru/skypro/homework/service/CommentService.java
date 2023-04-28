package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;

import java.util.Collection;

public interface CommentService {
    Collection<CommentDTO> getCommentByIdAds(Integer id);
}
