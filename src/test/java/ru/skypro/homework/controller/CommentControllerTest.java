package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.CommentServiceImp;
import ru.skypro.homework.util.FileManager;
import ru.skypro.homework.util.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.util.Value.*;

class CommentControllerTest extends ControllerClassTest{

    @SpyBean
    private CommentServiceImp commentService;

    @MockBean
    private CommentRepository commentRepository;

    @SpyBean
    private UserService userService;

    @SpyBean
    private AdsService adsService;

    @Autowired
    private CommentMapper commentMapper;

    @MockBean
    private AdsRepository adsRepository;

    @MockBean
    private FileManager fileManager;

    @MockBean
    private PhotoService photoService;

    @Autowired
    private AdsMapper adsMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("getCommentByIdAds - получить комментарии по id ads out ResponseWrapperComment status 200")
    public void getCommentByIdAdsStatus200Test() throws Exception {
        int id = 1;
        Users users = givenUsers();

        List<Comment> comments = Value.givenCommentList();

        List<CommentDTO> commentDTO = givenCommetDTOLIST(comments);

        ResponseWrapperComment wrapperComment = givenResponseWrapperComment(commentDTO);

        doReturn(users).when(userService).getUser();

        when(commentRepository.findByAds_Id(id)).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}/comments", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wrapperComment)));
    }

    @Test
    @WithMockUser
    @DisplayName("createComment - создание коментария in id out CommentDTO status 200")
    public void createCommentStatus200Test() throws Exception {
        int id = 1;
        Users users = givenUsers();

        JSONObject jsonCommentDTO = givenJsonCommentDTO();

        Ads ads = givenAds();


        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(id)).thenReturn(Optional.of(ads));

        Comment commentFirst = givenCommentFirst();

        when(commentRepository.save(any(Comment.class))).thenReturn(commentFirst);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ads/{id}/comments", id)
                        .content(jsonCommentDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Comment> commentArgCap = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentArgCap.capture());
        Comment comment = commentArgCap.getValue();
        assertThat(commentFirst.getText()).isEqualTo(comment.getText());
        assertThat(commentFirst.getUsers()).isEqualTo(comment.getUsers());
        assertThat(commentFirst.getAds()).isEqualTo(comment.getAds());
    }

    @Test
    @WithMockUser
    @DisplayName("createComment - создание коментария in id out CommentDTO status 404")
    public void createCommentStatus404Test() throws Exception {
        int id = 2;
        Users users = givenUsers();

        JSONObject jsonCommentDTO = givenJsonCommentDTO();

        doReturn(users).when(userService).getUser();

        when(adsRepository.findById(id)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ads/{id}/comments", id)
                        .content(jsonCommentDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteComment - удаление comment in idComment idAds out status 200")
    public void deleteCommentStatus200Test() throws Exception {
        int adId = 1;
        int commentId = 1;

        Users users = givenUsers();

        Comment comment = givenCommentFirst();

        doReturn(users).when(userService).getUser();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        doNothing().when(commentRepository).delete(comment);

        when(commentRepository.findCommentByIdAndUsersId(commentId, users.getId())).thenReturn(Optional.of(comment));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/ads/{adId}/comments/{commentId}", adId, commentId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(commentRepository, timeout(1)).delete(comment);
    }

    @Test
    @WithMockUser
    @DisplayName("deleteComment - удаление comment in idComment idAds out status 404")
    public void deleteCommentStatus404Test() throws Exception {
        int adId = 1;
        int commentId = 2;

        Users users = givenUsers();

        Comment comment = givenCommentFirst();

        doReturn(users).when(userService).getUser();

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        when(commentRepository.findCommentByIdAndUsersId(commentId, users.getId())).thenReturn(Optional.of(comment));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{adId}/comments/{commentId}", adId, commentId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(commentRepository,never()).delete(comment);
    }

    @Test
    @WithMockUser
    @DisplayName("updateComment - update comment in adsId commentId CommentDTO out CommentDTO status 200")
    public void updateCommentStatus200Test() throws Exception{
        int adId = 1;
        int commentId = 1;

        Users users = givenUsers();

        JSONObject jsonCommentDTOUpdate = givenJsonCommentDTOUpdate();

        Comment comment = givenCommentFirst();

        doReturn(users).when(userService).getUser();

        when(commentRepository.findCommentByIdAndUsersId(commentId, users.getId())).thenReturn(Optional.of(comment));

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{adsId}/comments/{commentId}", adId, commentId)
                        .content(jsonCommentDTOUpdate.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Comment> commentArgCap = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, timeout(1)).save(commentArgCap.capture());
        Comment commentAct = commentArgCap.getValue();
        assertThat("update comment").isEqualTo(commentAct.getText());
    }

    private List<CommentDTO> givenCommetDTOLIST(List<Comment> comments) {
        return comments.stream()
                .map(commentMapper::commentToCommentDTO)
                .collect(Collectors.toList());
    }
}
