package com.test.testlog.contoller;

import com.test.testlog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2024.01.25 데이터를 검증하는 이유 -> 검증을 하면 안전하게 믿고 저장할 수 있다.
 * 1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
 * 2. client bug로 값이 누락될 수 있다.
 * 3. 외부에 나쁜 사람이 값을 의미로 조작해서 보낼 수 있다.
 * 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
 * 5. 서버 개발자의 편안함을 위해
 */
@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) {
        log.info("params={}", params);
        /*
        String title = params.getTitle();
        if (title == null || title.equals("")) {  // 빈 String 값은 ? 더 검증해야 할게 생각보다 더 있을걸?
            // 이런 형태의 검증은 검증할 데이터가 많아지면 똑같은 코드가 너무 많이 들어가게 도니다.
            // -> 무언가 3번 이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다. (개발 tip)
            // -> 필드가 100개가 된다고 하면 검증 누락 가능성이 있다.
            // -> 생각보다 검증해야 할게 많다. (꼼꼼하지 않을 수 있다.)
            // -> 중요? 뭔가 개발자스럽지가 않다 ㅋㅋ
            throw new IllegalArgumentException("Title 값은 필수입니다.");
        }
        String content = params.getContent();
        if (content == null || content.equals("")) {
            throw new IllegalArgumentException("");
        }
         */
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);

            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();
            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }
        return Map.of();
    }
}
