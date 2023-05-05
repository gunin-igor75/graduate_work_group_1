package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentReq;
import ru.skypro.homework.dto.ResponseWrapperComment;

public interface CommentService {
    ResponseWrapperComment getCommentByIdAds(int id);

    boolean deleteComment(int adId, int commentId);

    CommentDTO updateComment(int adId, int commentId, CommentReq commentReq);

    CommentDTO createComment(int id, CommentReq commentReq);
}
