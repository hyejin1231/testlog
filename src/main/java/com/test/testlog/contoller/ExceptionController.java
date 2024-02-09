package com.test.testlog.contoller;

import com.test.testlog.exception.InvalidRequest;
import com.test.testlog.exception.TestlogException;
import com.test.testlog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static java.lang.String.*;

/**
 * 이 코드도 약간 문제는 있다고 함.. 개선 필요 !!
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public /*Map<String, String> */ErrorResponse ErrorMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        /*
        FieldError fieldError = e.getFieldError();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        Map<String, String> response = new HashMap<>();
        response.put(field, message);

        */
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .validation(new HashMap<>())
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TestlogException.class)
    public ResponseEntity<ErrorResponse> postNotFound(TestlogException e) {
        int getStatusCode = e.getStatusCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(valueOf(getStatusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        /* 근데 이렇게 하는 방법은.. 예외마다 이렇게 하기에는 무리가 있기 때문에 지양하는게 좋다.
        if (e instanceof InvalidRequest) {
            InvalidRequest invalidRequest = (InvalidRequest) e;
            String fieldName = invalidRequest.getFieldName();
            String message = invalidRequest.getMessage();
            response.getValidation().put(fieldName, message);
        }
         */

        return ResponseEntity.status(getStatusCode)
                .body(response);

    }


}
