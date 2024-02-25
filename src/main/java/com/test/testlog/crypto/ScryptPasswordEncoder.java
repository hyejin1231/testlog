package com.test.testlog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("default") // 기본 환경일 때 의존 관계 주입
@Component
public class ScryptPasswordEncoder implements PasswordEncoder{
   private static  final SCryptPasswordEncoder passwordEncoder =
           new SCryptPasswordEncoder(16, 8, 1, 32, 64);

   @Override
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
