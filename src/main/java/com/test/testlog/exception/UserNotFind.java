package com.test.testlog.exception;

public class UserNotFind extends TestlogException{

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFind() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
