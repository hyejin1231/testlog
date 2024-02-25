package com.test.testlog.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class PasswordEncoder {
   private static  final SCryptPasswordEncoder passwordEncoder =
           new SCryptPasswordEncoder(16, 8, 1, 32, 64);

    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
