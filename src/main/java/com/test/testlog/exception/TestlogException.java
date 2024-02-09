package com.test.testlog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 우리 비지니스에 맞는 최상위 Exception
 */
@Getter
public abstract class TestlogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public TestlogException(String message) {
        super(message);
    }

    public TestlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }
}
