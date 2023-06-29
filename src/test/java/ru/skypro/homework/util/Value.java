package ru.skypro.homework.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class Value {

    public static Users givenUsers() {
        return Users.builder()
                .id(1)
                .role(Role.USER)
                .email("user@mail.ru")
                .image("/image/1")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .password("$2a$12$Dn88gLtjUPpzOfajqvrLzu9hwI/nlahRKZm9s9O4wP/n0SgWmU22S")
                .build();
    }

    public static Users givenAdmin() {
        return Users.builder()
                .id(1)
                .role(Role.ADMIN)
                .email("user@mail.ru")
                .image("/image/1")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .password("$2a$12$Dn88gLtjUPpzOfajqvrLzu9hwI/nlahRKZm9s9O4wP/n0SgWmU22S")
                .build();
    }

    public static UserDTO givenUserDTO() {
        return UserDTO.builder()
                .id(1)
                .email("user@mail.ru")
                .firstName("igor")
                .lastName("igoreck")
                .phone("+79139792520")
                .build();
    }

    public static CreateAds givenCreateAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return CreateAds.builder()
                .description(description)
                .title(title)
                .price(price)
                .build();
    }

    public static CreateAds givenCreateAdsBad() {
        String description = "cat description";
        String title = givenRandomString(260);
        Integer price = 1000;
        return CreateAds.builder()
                .description(description)
                .title(title)
                .price(price)
                .build();
    }

    public static Ads givenAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return Ads.builder()
                .id(1)
                .users(givenUsers())
                .image("/image/1")
                .title(title)
                .description(description)
                .price(price)
                .build();
    }

    public static AdsDTO givenAdsDTO() {
        String title = "add title ads";
        Integer price = 1000;
        return AdsDTO.builder()
                .author(1)
                .pk(1)
                .price(price)
                .title(title)
                .image("/image/1")
                .build();
    }

    public static FullAds givenFullAds() {
        String description = "cat description";
        String title = "add title ads";
        Integer price = 1000;
        return FullAds.builder()
                .pk(1)
                .title(title)
                .description(description)
                .authorFirstName("igor")
                .authorLastName("igoreck")
                .email("user@mail.ru")
                .price(price)
                .image("/image/1")
                .phone("+79139792520")
                .build();
    }


    public static List<Ads> givenListAds() {
        Ads ads1 = Ads.builder()
                .id(1)
                .users(givenUsers())
                .title("add title ads first")
                .description("ads description")
                .price(1000)
                .build();
        Ads ads2 = Ads.builder()
                .id(2)
                .users(givenUsers())
                .title("add title ads second")
                .description("ads description")
                .price(2000)
                .build();
        return List.of(ads1, ads2);
    }

    public static List<AdsDTO> givenListAdsDTO() {
        AdsDTO adsDTO1 = AdsDTO.builder()
                .pk(1)
                .author(1)
                .title("add title ads first")
                .price(1000)
                .build();
        AdsDTO adsDTO2 = AdsDTO.builder()
                .pk(2)
                .author(1)
                .title("add title ads second")
                .price(2000)
                .build();
        return List.of(adsDTO2, adsDTO1);
    }

    public static Photo givenPhoto() {
        return Photo.builder()
                .filePath("image/image.jpg")
                .fileSize(10000)
                .id(1)
                .mediaType("image/jpeg")
                .build();
    }

    public static Photo givenPhotoTest(MockMultipartFile file) {
        return Photo.builder()
                .id(1)
                .fileSize(file.getSize())
                .mediaType(file.getContentType())
                .filePath("src/test/" + file.getOriginalFilename())
                .build();
    }

    public static String givenRandomString(int n){
        return RandomStringUtils.randomAlphanumeric(2);
    }

    public static JSONObject givenJsonUserDTOWithChanges() throws Exception {
        Integer id = 1;
        String email = "user@mail.ru";
        String firstName = "oleg";
        String lastName = "olegok";
        String phone = "+79139792535";

        JSONObject jsonUserDTO = new JSONObject();
        jsonUserDTO.put("id", id);
        jsonUserDTO.put("email", email);
        jsonUserDTO.put("firstName", firstName);
        jsonUserDTO.put("lastName", lastName);
        jsonUserDTO.put("phone", phone);

        return jsonUserDTO;
    }

    public static JSONObject givenJsonUserDTOWithoutChanges() throws Exception {
        Integer id = 1;
        String email = "user@mail.ru";
        String firstName = "igor";
        String lastName = "igoreck";
        String phone = "+79139792520";

        JSONObject jsonUserDTO = new JSONObject();
        jsonUserDTO.put("id", id);
        jsonUserDTO.put("email", email);
        jsonUserDTO.put("firstName", firstName);
        jsonUserDTO.put("lastName", lastName);
        jsonUserDTO.put("phone", phone);

        return jsonUserDTO;
    }

    public static JSONObject givenJsonUserDTOBad() throws Exception {
        Integer id = 1;
        String email = "user@mail.ru";
        String firstName = "igor";
        String lastName = "igoreck";
        String phone = "+11111111111";

        JSONObject jsonUserDTO = new JSONObject();
        jsonUserDTO.put("id", id);
        jsonUserDTO.put("email", email);
        jsonUserDTO.put("firstName", firstName);
        jsonUserDTO.put("lastName", lastName);
        jsonUserDTO.put("phone", phone);

        return jsonUserDTO;
    }

    public static JSONObject givenNewPassword(String currentPassword, String newPassword) throws JSONException {
        JSONObject jsonPassword = new JSONObject();
        jsonPassword.put("currentPassword", currentPassword);
        jsonPassword.put("newPassword", newPassword);
        return jsonPassword;
    }

    public static JSONObject givenJsonLoginReq(String username, String password) throws JSONException {
        JSONObject jsonLoginReq = new JSONObject();
        jsonLoginReq.put("username", username);
        jsonLoginReq.put("password", password);
        return jsonLoginReq;
    }

    public static JSONObject givenJsonRegisterReq() throws JSONException {
        String username = "user@mail.ru";
        String firstName = "igor";
        String lastname = "igoreck";
        String phone = "+79139792520";
        String password = "11111111";

        JSONObject jsonRegisterReq = new JSONObject();
        jsonRegisterReq.put("username", username);
        jsonRegisterReq.put("firstName", firstName);
        jsonRegisterReq.put("lastName", lastname);
        jsonRegisterReq.put("phone", phone);
        jsonRegisterReq.put("password", password);
        return jsonRegisterReq;
    }

    public static JSONObject givenJsonRegisterReqBad() throws JSONException {
        String username = "user@mail.ru";
        String firstName = "igor";
        String lastname = "igoreck";
        String phone = "+79139792520";
        String password = Value.givenRandomString(256);

        JSONObject jsonRegisterReq = new JSONObject();
        jsonRegisterReq.put("username", username);
        jsonRegisterReq.put("firstName", firstName);
        jsonRegisterReq.put("lastName", lastname);
        jsonRegisterReq.put("phone", phone);
        jsonRegisterReq.put("password", password);
        return jsonRegisterReq;
    }

    public static JSONObject givenJsonCreateAds() throws JSONException {
        String title = "add title ads";
        String description  = "update ads test";
        Integer price = 1000;
        JSONObject jsonCreateAds = new JSONObject();
        jsonCreateAds.put("title", title);
        jsonCreateAds.put("description", description);
        jsonCreateAds.put("price", price);
        return jsonCreateAds;
    }

    public static ResponseWrapperAds givenResponseWrapperAds(List<AdsDTO> adsDTO) {
        return ResponseWrapperAds.builder()
                .count(adsDTO.size())
                .results(adsDTO)
                .build();
    }

    public static Comment givenCommentFirst() {
        return Comment.builder()
                .createdAt(Instant.now())
                .text("text comment first")
                .users(givenUsers())
                .ads(givenAds())
                .id(1)
                .build();
    }

    public static Comment givenCommentSecond() {
        return Comment.builder()
                .createdAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .text("text comment second")
                .users(givenUsers())
                .id(2)
                .build();
    }

    public static CommentDTO givenCommentDTO() {
        return CommentDTO.builder()
                .text("update comment")
                .build();
    }

    public static List<Comment> givenCommentList() {
        return List.of(givenCommentFirst(), givenCommentSecond());
    }


    public static ResponseWrapperComment givenResponseWrapperComment(List<CommentDTO> comments) {
        return ResponseWrapperComment.builder()
                .count(comments.size())
                .results(comments)
                .build();
    }

    public static JSONObject givenJsonCommentDTO() throws JSONException {
        JSONObject jsonCommentDTO = new JSONObject();
        jsonCommentDTO.put("text", "text comment first");
        return jsonCommentDTO;
    }

    public static JSONObject givenJsonCommentDTOUpdate() throws JSONException {
        JSONObject jsonCommentDTO = new JSONObject();
        jsonCommentDTO.put("text", "update comment");
        return jsonCommentDTO;
    }
}
