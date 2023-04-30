package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception_handling.AdsNotFoundException;
import ru.skypro.homework.exception_handling.UserNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {
    private final AdsRepository adsRepository;
    private final UserService userService;
    private final AdsMapper mapper;

    @Override
    public FullAdsDTO getById (Integer id) {
       Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
       FullAdsDTO fullAdsDTO = mapper.adsToFullAdsDTO(ads);
       Users author = getAuthor();
       fullAdsDTO.setAuthorFirstName(author.getFirstName());
       fullAdsDTO.setAuthorLastName(author.getLastName());
       fullAdsDTO.setEmail(author.getEmail());
       fullAdsDTO.setPhone(author.getPhone());

       return fullAdsDTO;
    }

    @Override
    public Collection <AdsDTO> getAllAds() {

        return adsRepository.findAll().stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Collection <AdsDTO> getMeAds() {
        Integer authorId = getAuthor().getId();

        return adsRepository.finByAuthorId(authorId).stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());

    }

    @Override
    public AdsDTO createAds(String image, Integer price, String  title, String description) {
        Integer authorId = getAuthor().getId();
        Users author = userService.findUserById(authorId);
        Ads ads = new Ads();
        ads.setImage(image);
        ads.setUsers(author);
        ads.setPrice(price);
        ads.setTitle(title);
        ads.setDescription(description);
        adsRepository.save(ads);

       return mapper.adsToAdsDTO(ads);

    }

    @Override
    public void deleteAds(Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        adsRepository.delete(ads);

    }

    @Override
    public AdsDTO updateAds(Integer id, Integer price, String title, String description) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        ads.setPrice(price);
        ads.setTitle(title);
        ads.setDescription(description);
        adsRepository.save(ads);
        return mapper.adsToAdsDTO(ads);
    }
    @Override
    public String updateAdsImage(Integer id, String image) {
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        ads.setImage(image);
        adsRepository.save(ads);
        return "Ads image is updated! ";
    }

    private Users getAuthor (){
    Users author = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return author;
    }

}
