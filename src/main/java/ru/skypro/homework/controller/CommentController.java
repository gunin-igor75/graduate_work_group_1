package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.service.CommentService;

import java.util.Collection;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("{id}/comments")
    public ResponseEntity<Collection<CommentDTO>> getCommentByIdAds(@PathVariable("id") Integer id) {
        Collection<CommentDTO> comments = commentService.getCommentByIdAds(id);
        return ResponseEntity.ok(comments);
    }
}
