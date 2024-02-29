package com.test.testlog.config;

import java.io.IOException;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.util.AntPathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.config.filter.EmailPasswordAuthFilter;
import com.test.testlog.config.handler.Http401Handler;
import com.test.testlog.config.handler.Http403Handler;
import com.test.testlog.config.handler.LoginFailHandler;
import com.test.testlog.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity/*(debug = true) // debug : true, 좀 더 자세하게 설명 나옴 (운영 환경에서는 no)*/
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
	private final ObjectMapper objectMapper;
	
	private final UserRepository userRepository;
	
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
										.requestMatchers("/auth/signup").permitAll()
//										.requestMatchers("/admin").access(new WebExpressionAuthorizationManager("hasRole('ADMIN')  AND hasAuthority('WRITE')"))
//										.requestMatchers("/user").hasAnyRole("USER", "ADMIN") // ROLE_USER에서 ROLE 생략 가능
//										.requestMatchers("/admin").hasRole("ADMIN") // ROLE_ADMIN에서 ROLE 생략 가능
										.anyRequest().authenticated()
				)
				.addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//				.formLogin( // 로그인 폼 설정
//						form ->
//								form.loginPage("/auth/login")
//										.loginProcessingUrl("/auth/login")
//										.usernameParameter("username")
//										.passwordParameter("password")
//										.defaultSuccessUrl("/")
//										.failureHandler(new LoginFailHandler(objectMapper))
//				)
				.exceptionHandling( // 예외 핸들러
									e -> {
										e.accessDeniedHandler(new Http403Handler(objectMapper));
										e.authenticationEntryPoint(new Http401Handler(objectMapper));
									}
//						e -> {
//							e.accessDeniedHandler(new AccessDeniedHandler()
//							{
//								@Override
//								public void handle(HttpServletRequest request,
//										HttpServletResponse response,
//										AccessDeniedException accessDeniedException)
//										throws IOException, ServletException
//								{
//									log.info("403", accessDeniedException);
//								}
//							});
//						}
				)
				.rememberMe( // remember me
						rm ->
								rm.rememberMeParameter("remember")
										.alwaysRemember(false)
										.tokenValiditySeconds(2592000) // 30일
				)
				.csrf(AbstractHttpConfigurer::disable)  // TODO : CSRF 가 뭔지 ?
				.build();
	}
	
	@Bean
	public EmailPasswordAuthFilter emailPasswordAuthFilter()
	{
		EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/")); // 성공에 대한 처리 핸들러
		filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository()); // 이게 꼭 해줘야 세션이 정상 발급된다 !!
		
		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		rememberMeServices.setAlwaysRemember(true);
		rememberMeServices.setValiditySeconds(3600 * 24 * 30);
		filter.setRememberMeServices(rememberMeServices);
		return filter;
	}
	
	@Bean
	public AuthenticationManager authenticationManager()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(provider);
	}
	
	
	// InMemory방식
//	@Bean
	public UserDetailsService InMemoryUserDetailsService()
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails userDetails = User.withUsername("testlog")
				.password("1234").roles("AMDIN").build();
		manager.createUser(userDetails);
		return manager;
	}
	
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository)
	{
		return username -> {
			com.test.testlog.domain.User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
			
			return new UserPrincipal(user);
		};
	}
	
	
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
//		return NoOpPasswordEncoder.getInstance(); // encoder 안하는 클래스
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return new SCryptPasswordEncoder(16, 8, 1, 32,64);
	}
}
