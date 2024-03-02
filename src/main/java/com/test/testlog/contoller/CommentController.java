package com.test.testlog.contoller;

import com.test.testlog.request.comment.CommentCreate;
import com.test.testlog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comment")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate commentCreate) {
        commentService.write(postId, commentCreate);
    }
}
