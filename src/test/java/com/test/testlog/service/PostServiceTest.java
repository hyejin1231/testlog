package com.test.testlog.service;

import com.test.testlog.domain.Post;
import com.test.testlog.exception.PostNotFound;
import com.test.testlog.repository.PostRepository;
import com.test.testlog.request.PostCreate;
import com.test.testlog.request.PostEdit;
import com.test.testlog.request.PostSearch;
import com.test.testlog.response.PostResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
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
    
    
    @DisplayName("글 1개를 조회하는데 없는 blogId 를 조회하면 오류가 발생한다.")
    @Test
    void getWithNoBlogId() {
        // given
        Post post = Post.builder().title("제목 1").content("내용 1").build();
        postRepository.save(post);
        
        // when then
        Assertions.assertThatThrownBy(() -> {
                    postService.get(2L);
//                }).isInstanceOf(IllegalArgumentException.class)
                }).isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
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

    @Test
    @DisplayName("글 1 페이지 조회")
    void getOneList() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return
                            Post.builder().title("test 제목 - " + i).content("test 내용 - " + i).build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        List<PostResponse> posts = postService.getList(0);

        // then
        assertThat(posts).hasSize(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("test 제목 - 1");
        assertThat(posts.get(9).getTitle()).isEqualTo("test 제목 - 10");
    }

    @Test
    @DisplayName("글 1 페이지 조회")
    void getListWithQueryDsl() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return
                            Post.builder().title("test 제목 - " + i).content("test 내용 - " + i).build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        List<PostResponse> posts = postService.getListWithQueryDsl(0);

        // then
        assertThat(posts).hasSize(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("test 제목 - 30");
        assertThat(posts.get(9).getTitle()).isEqualTo("test 제목 - 21");
    }

    @Test
    @DisplayName("글 1 페이지 조회")
    void getListWithQueryDslAndPostSearch() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return
                            Post.builder().title("test 제목 - " + i).content("test 내용 - " + i).build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder().page(1).build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertThat(posts).hasSize(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("test 제목 - 30");
        assertThat(posts.get(9).getTitle()).isEqualTo("test 제목 - 21");
    }
    
    
    @Test
    @DisplayName("글 제목 수정 테스트")
    void edit() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        
        postRepository.save(post);
        
        // when
        PostEdit postEdit = PostEdit.builder()
                .title("testLog edit")
                .content("test log content")
                .build();
        
        postService.edit(post.getId(), postEdit);
        
        // then
        Post editPost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException(post.getId() + " 해당 글이 존재하지 않습니다."));
        assertThat(editPost.getTitle()).isEqualTo("testLog edit");
        assertThat(editPost.getContent()).isEqualTo("test log content");
    }
    
    @Test
    @DisplayName("글 내용 수정 테스트")
    void contentEdit() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        
        postRepository.save(post);
        
        // when
        PostEdit postEdit = PostEdit.builder()
                .title("testLog")
                .content("test log content")
                .build();
        
        postService.edit(post.getId(), postEdit);
        
        // then
        Post editPost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException(post.getId() + " 해당 글이 존재하지 않습니다."));
        assertThat(editPost.getTitle()).isEqualTo("testLog");
        assertThat(editPost.getContent()).isEqualTo("test log content");
    }
    
    @Test
    @DisplayName("글 내용 수정 테스트")
    void contentEdit2() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        
        postRepository.save(post);
        
        // when
        PostEdit postEdit = PostEdit.builder()
                .content("test log content")
                .build();
        
        postService.edit(post.getId(), postEdit);
        
        // then
        Post editPost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException(post.getId() + " 해당 글이 존재하지 않습니다."));
        assertThat(editPost.getTitle()).isEqualTo("testLog");
        assertThat(editPost.getContent()).isEqualTo("test log content");
    }
    
    @Test
    @DisplayName("존재하지 않는 글 내용 수정 테스트")
    void EditWhenNoBlog() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        
        postRepository.save(post);
        
        // when
        PostEdit postEdit = PostEdit.builder()
                .content("test log content")
                .build();
        
        // then
        assertThatThrownBy(() -> {
            postService.edit(post.getId() + 1, postEdit);
        }).isInstanceOf(PostNotFound.class).hasMessage("존재하지 않는 글입니다.");
    }
    
    
    @DisplayName("게시글 삭제")
    @Test
    void delete() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        postRepository.save(post);
        
        // when
        postService.delete(post.getId());
        
        // then
        long count = postRepository.count();
        assertThat(count).isEqualTo(0);
    }
    
    @DisplayName("존재하지 않는 게시글 삭제")
    @Test
    void deleteWhenNoBlog() {
        // given
        Post post = Post.builder()
                .title("testLog")
                .content("test log content")
                .build();
        postRepository.save(post);
        
        // when
        postService.delete(post.getId());
        
        // then
        assertThatThrownBy(() -> {
            postService.get(post.getId() + 1L);
        }).isInstanceOf(PostNotFound.class).hasMessage("존재하지 않는 글입니다.");
    }
    

}