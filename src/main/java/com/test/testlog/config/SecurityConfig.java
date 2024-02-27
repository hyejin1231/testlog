package com.test.testlog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity/*(debug = true) // debug : true, 좀 더 자세하게 설명 나옴 (운영 환경에서는 no)*/
public class SecurityConfig
{
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer()
	{
		return web -> web.ignoring().requestMatchers("/favicon.ico", "/error")
				.requestMatchers(PathRequest.toH2Console());
//				.requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}
	
	@Bean // 빈 등록 방식으로 바뀜
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		// lambda 방식으로 변경함
		return httpSecurity.
				authorizeHttpRequests(
						authorize ->
								authorize
										.requestMatchers("/auth/login").permitAll()
										.anyRequest().authenticated()
				)
				.formLogin( // 로그인 폼 설정
						form ->
								form.loginPage("/auth/login")
										.loginProcessingUrl("/auth/login")
										.usernameParameter("username")
										.passwordParameter("password")
										.defaultSuccessUrl("/")
				)
				.rememberMe( // remember me
						rm ->
								rm.rememberMeParameter("remember")
										.alwaysRemember(false)
										.tokenValiditySeconds(2592000) // 30일
				)
				.userDetailsService(userDetailsService())
				.csrf(AbstractHttpConfigurer::disable)  // TODO : CSRF 가 뭔지 ?
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails userDetails = User.withUsername("testlog")
				.password("1234").roles("AMDIN").build();
		manager.createUser(userDetails);
		return manager;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance(); // encoder 안하는 클래스
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
