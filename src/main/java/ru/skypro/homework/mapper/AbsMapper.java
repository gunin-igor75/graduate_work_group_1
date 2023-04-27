package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.entity.Ads;


@Mapper(componentModel = "spring")
public interface AbsMapper {

    @Mapping(target = "author", source = "users.id")
    AdsDTO absToAbsDTO(Ads ads);
    Ads absDTOToAbs(AdsDTO adsDTO);
}
