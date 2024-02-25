package com.test.testlog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test") // 테스트 환경일 때 의존관계 주입
@Component
public class PlainPasswordEncoder implements PasswordEncoder {

    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return rawPassword.equals(encryptedPassword);
    }
}
