package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис-класс определяющий логику создания, получения, изменения, удаления комментария в БД
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final AdsService adsService;

    private final CommentMapper mapper;

    /**
     * Получение комментариев по объявлению
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @return - обертка, содержащая комментарии и их количество
     */
    @Override
    public ResponseWrapperComment getResponseCommentsByAdsId(int id) {
        List<CommentDTO> comments = getCommentsByAdsId(id).stream()
                .sorted()
                .map(mapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return ResponseWrapperComment.builder()
                .count(comments.size())
                .results(comments)
                .build();
    }

    /**
     * Создание и сохранение в БД комментария
     * @param id - индентификатор по каторому объявление хранится в БД не может быть {@code null}
     * @param commentDTO - сущность, содержащая все необходимы параметры для создания комментария
     * @return - сущность, содержащая все необходимы параметры для отображения
     * на фронте комментария
     */
    @Override
    public CommentDTO createComment(int id, CommentDTO commentDTO) {
        Ads ads = adsService.getAds(id);
        Users user = userService.getUser();
        Comment comment = new Comment();
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setText(commentDTO.getText());
        comment.setUsers(user);
        Comment persistentComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(persistentComment);
    }

    /**
     * Удаление из БД комментария
     * @param id - индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     */
    @Override
    public void deleteComment(int id) {
        Comment comment = findComment(id);
        commentRepository.delete(comment);
    }

    /**
     * Поиск комментария
     * @param id - индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     * @return - найденный комментарий из БД
     * @throws CommentNotFoundException - при отсутстивии комментария с id
     */
    @Override
    public Comment findComment(int id) {
        return commentRepository.findById(id).orElseThrow(() -> {
                    String message = "Comment with " + id + " is not in the database";
                    log.error(message);
                    return new CommentNotFoundException(message);
                }
        );
    }

    /**
     * Изменение комментария
     * @param id - индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     * @param commentDTO - {@code CommentDTO} - сущность, содержащая все необходимы параметры для отображения
     * на фронте комментария
     * @return - сохраненный комментарий преобразованный в {@code CommentDTO}
     */
    @Override
    public CommentDTO updateComment(int id, CommentDTO commentDTO) {
        Comment comment = findComment(id);
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(Instant.now());
        Comment newComment = commentRepository.save(comment);
        return mapper.commentToCommentDTO(newComment);
    }

    /**
     * Нахождение комментариев по id объявлению
     * @param id - индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     * @return - коллекция комментариев
     */
    @Override
    public List<Comment> getCommentsByAdsId(int id) {
        return commentRepository.findByAds_Id(id);
    }

    /**
     * Является ли пользователь хозяином комментария
     * @param commentId - индентификатор по каторому комментарий хранится в БД не может быть {@code null}
     * @param userId -индентификатор по каторому пользователь хранится в БД не может быть {@code null}
     * @return - {@code true} - успех,  {@code false} - неудача
     */
    @Override
    public boolean isOwnerComment(int commentId, Integer userId) {
        Optional<Comment> commentOrNull = commentRepository.findCommentByIdAndUsersId(commentId, userId);
        return commentOrNull.isPresent();
    }
}


















