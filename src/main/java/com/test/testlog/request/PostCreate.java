package com.test.testlog.request;

import com.test.testlog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {
    @NotBlank(message = "title 값은 필수입니다.")
    private String title;
    @NotBlank(message = "content값은 필수입니다.")
    private String content;

    /**
     * Builder 패턴의 장점
     * 1. 가독성이 좋다.
     * 2. 필요한 값만 받을 수 있다.
     * 3. 객체의 붊변성
     */
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title","제목에 '바보'를 포함할 수 없습니다.");
        }
    }
}
