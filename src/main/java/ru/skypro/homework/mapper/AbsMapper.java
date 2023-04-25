package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AbsDTO;
import ru.skypro.homework.entity.Ads;


@Mapper(componentModel = "spring")
public interface AbsMapper {

    @Mapping(target = "authorId", source = "user.id")
    AbsDTO absToAbsDTO(Ads ads);
}
