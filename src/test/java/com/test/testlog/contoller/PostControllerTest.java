package com.test.testlog.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.domain.Post;
import com.test.testlog.repository.PostRepository;
import com.test.testlog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void posts() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
//                                .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}") // JSON 형태
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 값은 필수다. Title 값이 없으면 에러 메시지를 리턴한다.")
    void posts2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
                                .content(json)
//                                .content("{\"title\": \"\", \"content\": \"내용입니다.\"}") // JSON 형태
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("title 값은 필수입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 DB에 값이 저장된다.")
    void posts3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.!!")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
                                .content(json)
//                                .content("{\"title\": \"제목입니다.!!\", \"content\": \"내용입니다.\"}") // JSON 형태
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        assertThat(postRepository.count()).isEqualTo(1);
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.!!");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void get() throws Exception {
        // given
        Post givenPost = Post.builder()
                .title("글 제목!~~~~~~~~~~~`")
                .content("글 내용")
                .build();
            postRepository.save(givenPost);

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", givenPost.getId())
                .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(givenPost.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(givenPost.getTitle().substring(0,10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(givenPost.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getList() throws Exception {
        // given
        Post givenPost1 = Post.builder()
                .title("글 제목 1 ~~~~~~~~~~")
                .content("글 내용")
                .build();
        Post givenPost2 = Post.builder()
                .title("글 제목 2 ~~~~~~~~~~~")
                .content("글 내용")
                .build();
        Post givenPost3 = Post.builder()
                .title("글 제목 3 ~~~~~~~~~~~")
                .content("글 내용")
                .build();
        postRepository.saveAll(List.of(givenPost1, givenPost2, givenPost3));

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(givenPost1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("글 제목 1 ~~~~~~~~~~"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(givenPost1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(givenPost2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("글 제목 2 ~~~~~~~~~~~"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value(givenPost2.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(givenPost3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("글 제목 3 ~~~~~~~~~~~"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].content").value(givenPost3.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }
}