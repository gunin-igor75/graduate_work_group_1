package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.AdsNotFoundException;
import ru.skypro.homework.exception_handling.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdsRepository adsRepository;
    private final CommentMapper mapper;

    @Override
    public ResponseWrapperComment getResponseCommentByIdAds(int id) {
        List<CommentDTO> comments = commentRepository.findByAds_Pk(id).stream()
                .map(mapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return ResponseWrapperComment.builder()
                .count(comments.size())
                .results(comments)
                .build();
    }



    @Override
    public Comment findComment(Integer pk) {
        return commentRepository.findById(pk).orElseThrow(
                    CommentNotFoundException::new
        );
    }

    @Override
    public boolean deleteComment(int commentId) {
        Comment comment = findComment(commentId);
        try {
            commentRepository.delete(comment);
        } catch (EntityNotFoundException e) {
            log.warn("Comment does not exist {}", commentId);
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void deleteComments(List<Comment> comments) {
        comments.forEach(comment -> deleteComment(comment.getPk()));
    }

    @Override
    public CommentDTO updateComment(int adId, int commentId, CommentDTO commentDTO) {
        Comment comment = findComment(commentId);
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(Instant.now());
        Comment newComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(newComment);
    }

    @Override
    public CommentDTO createComment(int id, CommentDTO commentDTO) {
        Ads ads = adsRepository.findById(id).orElseThrow(
                AdsNotFoundException::new
        );
        Users user = userService.getAuthorizedUser();
        Comment comment = new Comment();
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setText(commentDTO.getText());
        comment.setUsers(user);
        Comment newComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(newComment);
    }

    @Override
    public List<Comment> getCommentByIdAds(int id) {
        return commentRepository.findByAds_Pk(id);
    }
}


















