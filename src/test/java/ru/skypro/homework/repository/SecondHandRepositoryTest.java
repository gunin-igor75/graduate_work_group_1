package ru.skypro.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Users;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
@Sql(scripts = "/data.sql")
class SecondHandRepositoryTest {

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.contexts", () -> "!prod");
    }

    @Test
    @DisplayName("findAdsByUserId - нахождение ads по id users")
    public void findAdsByUserIdTest() {
        List<Ads> adsFirst = adsRepository.findAdsByUserId(1);
        assertThat(adsFirst.size()).isEqualTo(2);

        List<Ads> adsSecond = adsRepository.findAdsByUserId(2);
        assertThat(adsSecond.size()).isEqualTo(2);

        List<Ads> adsThree = adsRepository.findAdsByUserId(3);
        assertThat(adsThree.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("findAdsByIdAndUsersId - нахождение ad по id ads и id users")
    public void findAdsByIdAndUsersIdTest() {
        Ads adsFirst = adsRepository.findAdsByIdAndUsersId(1, 1).orElse(new Ads());

        assertThat(adsFirst.getId()).isEqualTo(1);
        assertThat(adsFirst.getImage()).isEqualTo("/image/2");

        Ads adsSecond = adsRepository.findAdsByIdAndUsersId(10, 1).orElse(new Ads());
        assertThat(adsSecond.getId()).isEqualTo(null);
        assertThat(adsSecond.getImage()).isEqualTo(null);
        assertThat(adsSecond.getPrice()).isEqualTo(null);
        assertThat(adsSecond.getTitle()).isEqualTo(null);
    }

    @Test
    @DisplayName("findByAds_Id - нахождение comment по id ads")
    public void findByAds_IdTest() {
        List<Comment> commentsFirst = commentRepository.findByAds_Id(1);
        assertThat(commentsFirst.size()).isEqualTo(3);

        List<Comment> commentsSecond = commentRepository.findByAds_Id(2);
        assertThat(commentsSecond.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("findCommentByIdAndUsersId - поиск comment по id comment и id users")
    public void findCommentByIdAndUsersIdTest() {
        Comment commentFirst =
                commentRepository.findCommentByIdAndUsersId(4, 1).orElse(new Comment());
        assertThat(commentFirst.getId()).isEqualTo(4);

        Comment commentSecond =
                commentRepository.findCommentByIdAndUsersId(4, 10).orElse(new Comment());
        assertThat(commentSecond.getId()).isEqualTo(null);
    }


    @Test
    @DisplayName("findByEmail - поиск users по email")
    public void findByEmailTest() {
        Users userFirst = userRepository.findByEmail("user@mail.ru").orElse(new Users());
        assertThat(userFirst.getId()).isEqualTo(1);
        assertThat(userFirst.getFirstName()).isEqualTo("igor");
        assertThat(userFirst.getLastName()).isEqualTo("igoreck");
        assertThat(userFirst.getPassword()).isEqualTo("$2a$12$p5PSTNhGEPAwMPPlYcrK7u6KSGNlqIha5Zrgs7ar9/0Qx5wkQq/0G");

        Users usersSecond = userRepository.findByEmail("puper@mail.ru").orElse(new Users());
        assertThat(usersSecond.getId()).isEqualTo(null);
    }

    @Test
    @DisplayName("findPhotoByOwner - поиск картинки по хозяину in photoType id out photo")
    public void findPhotoByOwnerTest() {
        Photo photoFirst = photoRepository.findPhotoByOwner("Picture", 3).orElse(new Photo());
        assertThat(photoFirst.getId()).isEqualTo(4);
        assertThat(photoFirst.getFileSize()).isEqualTo(156014);
        assertThat(photoFirst.getFilePath()).isEqualTo("picture\\a915b911-be55-4545-5555-7a7d94e79a28.png");

        Photo photoSecond = photoRepository.findPhotoByOwner("Avatar", 3).orElse(new Photo());
        assertThat(photoSecond.getFilePath()).isEqualTo(null);
    }
}