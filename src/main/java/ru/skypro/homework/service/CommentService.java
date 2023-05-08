package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Comment;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentService {
    ResponseWrapperComment getResponseCommentByIdAds(int id);

    Comment findComment(Integer pk);

    boolean deleteComment(int commentId);

    @Transactional
    void deleteComments(List<Comment> comments);

    CommentDTO updateComment(int adId, int commentId, CommentDTO commentDTO);

    CommentDTO createComment(int id, CommentDTO commentDTO);

    List<Comment> getCommentByIdAds(int id);

}
