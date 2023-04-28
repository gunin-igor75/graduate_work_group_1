package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper mapper;


    @Override
    public Collection<CommentDTO> getCommentByIdAds(Integer id) {
        return commentRepository.findByAds_Pk(id).stream()
                .map(mapper::commentToCommentDTO)
                .collect(Collectors.toList());
    }
}
