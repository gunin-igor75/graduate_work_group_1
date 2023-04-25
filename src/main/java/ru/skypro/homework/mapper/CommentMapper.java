package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorId" , source = "user.id")
    @Mapping(target = "authorFirstName" , source = "user.firstName")
    @Mapping(target = "authorImage" , source = "user.image")
    @Mapping(target = "createdAt" , expression = "java(comment.getCreatedAt().getTime())")
    CommentDTO commentToCommentDTO(Comment comment);
}
