package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.entity.Comment;

/**
 * Интерфейс для преобразования коментариев
 */

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразование {@code Comment} в {@code CommentDTO}
     * @param comment - коментарий в форме для работы с БД
     * @return - коментарий в форме для работы с фронтом
     */
    @Mapping(target = "author" , source = "users.id")
    @Mapping(target = "authorFirstName" , source = "users.firstName")
    @Mapping(target = "authorImage" , source = "users.image")
    @Mapping(target = "createdAt" , expression = "java(comment.getCreatedAt().toEpochMilli())")
    @Mapping(target = "pk" , source = "id")
    CommentDTO commentToCommentDTO(Comment comment);

}
