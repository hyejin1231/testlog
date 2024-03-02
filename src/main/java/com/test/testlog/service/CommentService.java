package com.test.testlog.service;

import com.test.testlog.domain.Comment;
import com.test.testlog.domain.Post;
import com.test.testlog.exception.PostNotFound;
import com.test.testlog.repository.comment.CommentRepository;
import com.test.testlog.repository.post.PostRepository;
import com.test.testlog.request.comment.CommentCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate commentCreate) {

        // 1) 게시글 존재 여부 확인
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(commentCreate.getPassword());

        // 2)
        Comment comment = Comment.builder()
                .author(commentCreate.getAuthor())
                .password(encryptedPassword)
                .content(commentCreate.getContent())
                .build();

        post.addComment(comment);

    }
}
