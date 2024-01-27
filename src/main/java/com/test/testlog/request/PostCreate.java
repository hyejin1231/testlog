package com.test.testlog.request;

import jakarta.validation.constraints.NotBlank;
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

}
