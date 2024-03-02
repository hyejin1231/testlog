package com.test.testlog.contoller;

import com.test.testlog.request.comment.CommentCreate;
import com.test.testlog.request.comment.CommentDelete;
import com.test.testlog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comment")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate commentCreate) {
        commentService.write(postId, commentCreate);
    }

    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable Long commentId, @RequestBody @Valid CommentDelete commentDelete) {
        commentService.delete(commentId, commentDelete);
    }
}
