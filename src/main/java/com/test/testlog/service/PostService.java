package com.test.testlog.service;

import com.test.testlog.domain.Post;
import com.test.testlog.domain.PostEditor;
import com.test.testlog.domain.User;
import com.test.testlog.exception.PostNotFound;
import com.test.testlog.exception.UserNotFind;
import com.test.testlog.repository.post.PostRepository;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.post.PostCreate;
import com.test.testlog.request.post.PostEdit;
import com.test.testlog.request.post.PostSearch;
import com.test.testlog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(PostCreate postCreate, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFind::new);

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .user(user)
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));


        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB 글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.
    // 사실 다 조회하는 일은 없다. -> 보통 페이징을 한다.
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Paging 을 이용한 글 조회
     * @param page
     * @return
     */
    public List<PostResponse> getList(int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getListWithQueryDsl(int page) {
        return postRepository.getList(1).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit)
    {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

//        post.change(postEdit.getTitle(), postEdit.getContent());
        
        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent()).build();
        
        post.edit(postEditor);
        
        return new PostResponse(post);
    }
    
    public void delete(Long postId)
    {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
//                () -> new IllegalArgumentException("존재하지 않는 글입니다."));
        
        postRepository.delete(post);
    }
}
