package com.test.testlog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {
    @NotBlank(message = "Title 값은 필수입니다.")
    private String title;
    @NotBlank(message = "Content를 입력해주세요.")
    private String content;

}
