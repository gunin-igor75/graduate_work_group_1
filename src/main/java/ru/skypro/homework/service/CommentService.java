package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentReq;
import ru.skypro.homework.dto.ResponseWrapperComment;

public interface CommentService {
    ResponseWrapperComment getCommentByIdAds(Integer id);

    boolean deleteComment(Integer adId, Integer commentId);

    CommentDTO updateComment(Integer adId, Integer commentId, CommentReq commentReq);

    CommentDTO createComment(Integer aiD, CommentReq commentReq);
}
