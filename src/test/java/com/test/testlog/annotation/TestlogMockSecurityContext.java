package com.test.testlog.annotation;

import com.test.testlog.config.UserPrincipal;
import com.test.testlog.domain.User;
import com.test.testlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class TestlogMockSecurityContext implements WithSecurityContextFactory<TestlogMockUser> {

    private final  UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(TestlogMockUser annotation) {
        User user = User.builder().email(annotation.email()).name(annotation.name()).password(annotation.password()).build();
        userRepository.save(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_ADMIN");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userPrincipal, user.getPassword(), List.of(role));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }
}
