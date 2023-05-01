package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentReq;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.service.CommentService;


@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getCommentByIdAds(@PathVariable("id") Integer id) {
        ResponseWrapperComment comments = commentService.getCommentByIdAds(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable("id") Integer id,
                                                    @RequestBody CommentReq commentReq) {
        CommentDTO commentDTO = commentService.createComment(id, commentReq);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        boolean deleteComment = commentService.deleteComment(adId, commentId);
        return deleteComment ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("adId") Integer adId,
                                                    @PathVariable("commentId") Integer commentId,
                                                    @RequestBody CommentReq commentReq) {
        CommentDTO commentDTO = commentService.updateComment(adId, commentId, commentReq);
        return ResponseEntity.ok(commentDTO);
    }
}
