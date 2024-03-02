package com.test.testlog.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.domain.Comment;
import com.test.testlog.domain.Post;
import com.test.testlog.domain.User;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.repository.comment.CommentRepository;
import com.test.testlog.repository.post.PostRepository;
import com.test.testlog.request.comment.CommentCreate;
import com.test.testlog.request.comment.CommentDelete;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void writes() throws Exception {
        // given
        User user = User.builder()
                .name("testlog")
                .password("1234")
                .email("testlog@gmail.com")
                .build();
        User saveUser = userRepository.save(user);

        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(saveUser)
                .build();
        Post savePost = postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("testUser")
                .content("테스트 댓글 !!!!!!!!!!!!!!!!!!!!!!!")
                .password("1234567")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // when.. then
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comment", savePost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(commentRepository.count()).isEqualTo(1);
        Comment savedComment = commentRepository.findAll().get(0);
        assertThat(savedComment.getAuthor()).isEqualTo("testUser");
        assertThat(savedComment.getContent()).isEqualTo("테스트 댓글 !!!!!!!!!!!!!!!!!!!!!!!");
        assertThat(savedComment.getPassword()).isNotEqualTo("1234567");
        assertTrue(passwordEncoder.matches("1234567", savedComment.getPassword()));
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void delete() throws Exception {
        // given
        User user = User.builder()
                .name("testlog")
                .password("1234")
                .email("testlog@gmail.com")
                .build();
        User saveUser = userRepository.save(user);

        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(saveUser)
                .build();
        Post savePost = postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");
        Comment comment = Comment.builder()
                .author("testUser")
                .password(encryptedPassword)
                .content("으하ㅏ하ㅏ하핳하하하ㅏ하하하ㅏㅎㅎㅎ핳하하")
                .build();
        comment.setPost(savePost);
        Comment savedComment = commentRepository.save(comment);

        CommentDelete commentDelete = new CommentDelete("123456");

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{commentId}/delete", savedComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDelete))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}