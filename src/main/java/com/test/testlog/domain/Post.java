package com.test.testlog.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
