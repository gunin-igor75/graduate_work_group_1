package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Comment;

import java.util.List;

public interface CommentService {
    ResponseWrapperComment getResponseCommentsByAdsId(int id);

    Comment findComment(int id);

    void deleteComment(int id);

    void deleteCommentsByAdsId(int adsId);

    CommentDTO updateComment(int id, CommentDTO commentDTO);

    CommentDTO createComment(int id, CommentDTO commentDTO);

    List<Comment> getCommentsByAdsId(int id);

    boolean isOwnerComment(int commentId, Integer userId);
}
