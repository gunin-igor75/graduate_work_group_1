package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.exception_handling.AvatarNotFoundException;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.service.AvatarService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AvatarServiceImp implements AvatarService {

    private final AvatarRepository avatarRepository;

    @Override
    public Avatar getCurrentAvatarOrNew(Integer id) {
       return avatarRepository.findById(id).orElse(new Avatar());
    }

    @Override
    public Avatar getAvatarById(Integer id) {
        return avatarRepository.getReferenceById(id);
    }

    @Override
    public void createAvatar(Avatar avatar) {
        if (avatar == null) {
            throw new AvatarNotFoundException();
        }
        avatarRepository.save(avatar);
    }
}
