package com.test.testlog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * 여기에는 서비스의 정책을 넣지 마세요 !! 절대 !!
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 넣어줘야함
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    
    // setter 대신 change 메서드같은 걸 만들어서 사용하기
    // 이렇게 String 두개를 파라미터로 받으면 파라미터가 여러개 되면 버그를 발견하기 어려워짐
    // 이 방법도 괜찮지만 나중을 위해 다른 방법을 사용하기
    public void change(String title, String content)
    {
        this.title = title;
        this.content = content;
    }
    
    public PostEditor.PostEditorBuilder toEditor()
    {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }
    
    public void edit(PostEditor postEditor)
    {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }

    public Long getUserId() {
        return this.user.getId();
    }


    public void addComment(Comment comment) {
       comment.setPost(this);
        this.comments.add(comment);
    }
}

