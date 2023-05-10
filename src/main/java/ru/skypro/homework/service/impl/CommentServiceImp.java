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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
    public ResponseWrapperComment getResponseCommentsByAdsId(int id) {
        List<CommentDTO> comments = getCommentsByAdsId(id).stream()
                .map(mapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return ResponseWrapperComment.builder()
                .count(comments.size())
                .results(comments)
                .build();
    }


    @Override
    public Comment findComment(int id) {
        return commentRepository.findById(id).orElseThrow(
                CommentNotFoundException::new
        );
    }

    @Override
    public void deleteComment(int id) {
        Comment comment = findComment(id);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentsByAdsId(int adsId) {
        commentRepository.deleteCommentByAds_Id(adsId);
    }

    @Override
    public CommentDTO updateComment(int id, CommentDTO commentDTO) {
        Comment comment = findComment(id);
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
        Comment persistentComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(persistentComment);
    }

    @Override
    public List<Comment> getCommentsByAdsId(int id) {
        return commentRepository.findByAds_Id(id);
    }

    @Override
    public boolean isOwnerComment(int commentId, Integer userId) {
        Optional<Comment> commentOrNull = commentRepository.findCommentByIdAndUsersId(commentId, userId);
        return commentOrNull.isPresent();
    }
}


















