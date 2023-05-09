package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;


@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getCommentByIdAds(@PathVariable("id") int id) {
        ResponseWrapperComment comments = commentService.getResponseCommentByIdAds(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable("id") int id,
                                                    @Validated @RequestBody CommentDTO commentDTO) {
        CommentDTO commentNew = commentService.createComment(id, commentDTO);
        return commentNew != null ? ResponseEntity.ok(commentNew) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") int adsId,
                                           @PathVariable("commentId") int commentId) {
        boolean deleteComment = commentService.deleteComment(commentId);
        return deleteComment ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("adId") int adsId,
                                                    @PathVariable("commentId") int commentId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        CommentDTO commentNew = commentService.updateComment(adsId, commentId, commentDTO);
        return commentNew != null ? ResponseEntity.ok(commentNew) : ResponseEntity.notFound().build();
    }
}
