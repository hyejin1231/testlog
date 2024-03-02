package com.test.testlog.annotation;

import com.test.testlog.domain.User;
import com.test.testlog.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;

// TODO : 만약 어노테이션으로 사용한다면 여기 코드 더 추가해야 함 !
public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    private  UserRepository userRepository;
    private UserDetailsService userDetailsService;

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {

        String username = annotation.username();
        String password = annotation.password();

        User user = User.builder().email(username).password(password).build();
        userRepository.save(user);

        return null;
    }
}
