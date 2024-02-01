package com.test.testlog.service;

import com.test.testlog.domain.Post;
import com.test.testlog.repository.PostRepository;
import com.test.testlog.request.PostCreate;
import com.test.testlog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
     void clean() {
        // 데이터 클렌징 작업
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("글 작성")
    void write() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertThat(postRepository.count()).isEqualTo(1);
        Post post = postRepository.findAll().get(0);
        assertThat(post).extracting("title", "content").contains("제목입니다.", "내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void get() {
        // given
        Post givenPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        Post post = postRepository.save(givenPost);

        // 클라이언트 요구사항
            // json 응답에서 title 값 길이를 최대 10글자로 해주세요.

        // when
        PostResponse response = postService.get(post.getId());

        // then
        assertThat(response.getTitle()).isEqualTo(givenPost.getTitle());
        assertThat(response.getContent()).isEqualTo(givenPost.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getList() {
        // given
        Post givenPost1 = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        Post givenPost2 = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        Post givenPost3 = Post.builder()
                .title("foo")
                .content("bar")
                .build();
         postRepository.save(givenPost1);
        postRepository.save(givenPost2);
        postRepository.save(givenPost3);

        // when
        List<PostResponse> posts = postService.getList();

        // then
        assertThat(posts).hasSize(3);


    }
}