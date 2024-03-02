package com.test.testlog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithSecurityContext(factory = TestlogMockSecurityContext.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestlogMockUser {

    String email() default "testlog@gmail.com";
    String name() default "testlog";

    String password() default "1234";

    String role() default "ROLE_ADMIN";
}
