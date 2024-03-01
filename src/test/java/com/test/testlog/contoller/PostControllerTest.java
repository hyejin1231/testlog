package com.test.testlog.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.annotation.TestlogMockUser;
import com.test.testlog.domain.Post;
import com.test.testlog.domain.User;
import com.test.testlog.repository.PostRepository;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.PostCreate;
import com.test.testlog.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Product Controller Test
 */
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

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

//    @Test
//    @DisplayName("글 작성 요청 시 title 값은 필수다. Title 값이 없으면 에러 메시지를 리턴한다.")
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
    @TestlogMockUser
//    @WithMockUser(username = "testlog@gmail.com" , roles = {"ADMIN"}, password = "1234") // 로그인 인증이 됐다고 가정
    @DisplayName("글 작성 요청 시 DB에 값이 저장된다.")
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

//    @Test
//    @DisplayName("글 작성 요청 시 DB에 값이 저장된다. + 인증 추가 ")
    void posts4() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.!!")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts?authorization=testlog")
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

//    @Test
//    @DisplayName("글 작성 요청 시 DB에 값이 저장된다. + 인증 추가 ")
    void posts5() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.!!")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .header("authorization", "testlog")
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

    @Test
    @DisplayName("글 1 페이지  조회")
    void getOneList() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return
                            Post.builder().title("test 제목 - " + i).content("test 내용 - " + i).build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/posts?page=0")
                        .contentType(APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("test 제목 - 1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 1 페이지  조회")
    void getOneListWithQueryDslAndPostSearch() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return
                            Post.builder().title("test 제목 - " + i).content("test 내용 - " + i).build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/v3/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("test 제목 - 30"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    @TestlogMockUser
//    @WithMockUser(username = "testlog@gmail.com" , roles = {"ADMIN"}, password = "1234") // 로그인 인증이 됐다고 가정
    @DisplayName("글 제목 수정")
    void edit() throws Exception
    {
        User user = userRepository.findAll().get(0);
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);
        
        PostEdit postEdit = PostEdit.builder().title("글 제목 수정").content("글 내용").build();
        
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
                                .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("글 제목 수정"))
                .andDo(MockMvcResultHandlers.print());
        
    }

    @TestlogMockUser
    @DisplayName("게시글 삭제")
    @Test
    void delete() throws Exception
    {
        // given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);
        
        // when then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void getNoPost() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/posts/{postId}", 1L)
                                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

//    @WithMockUser(username = "testlog@gmail.com" , roles = {"ADMIN"}, password = "1234") // 로그인 인증이 됐다고 가정
    @TestlogMockUser
    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void editNoPost() throws Exception {

        PostEdit postEdit = PostEdit.builder().title("글 제목 수정").content("글 내용").build();

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

//    `@WithMockUser(username = "testlog@gmail.com" , roles = {"ADMIN"}, password = "1234") // 로그인 인증이 됐다고 가정`
    @TestlogMockUser
    @Test
    @DisplayName("게시글 작성 시, 제목에 '바보' 단어는 포함될 수 없다.")
    void postsNotContainsTitleWord() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다. 바보")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}