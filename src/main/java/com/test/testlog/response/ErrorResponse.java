package com.test.testlog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *  "code" : "400",
 *  "message" : "잘못된 요청입니다.",
 *  "validateion" : {
 *      "title" : "값을 입력해주세요."
 *    }
 * }
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 빈 값은 응답은 주지 않도록 하는 방법 -> 개인적으로 호돌맨은 선호하지 않는다고 함
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation ;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }


}
