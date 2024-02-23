package com.test.testlog.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.test.testlog.domain.User;
import com.test.testlog.exception.AlreadyExistsException;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.SignUp;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthService authService;
	
	@AfterEach
	void setUp()
	{
		userRepository.deleteAll();
	}
	
	@DisplayName("회원가입 성공")
	@Test
	void signUp() {
	    // given
		SignUp signUp = SignUp.builder()
				.email("testlog@gmail.com")
				.password("1234").name("testlog").build();
		
		// when
		authService.signUp(signUp);
	    
	    // then
		assertThat(userRepository.count()).isEqualTo(1);
		User user = userRepository.findAll().iterator().next();
		assertThat(user.getEmail()).isEqualTo("testlog@gmail.com");
		assertThat(user.getName()).isEqualTo("testlog");
		assertThat(user.getPassword()).isNotEqualTo("1234");
		assertThat(user.getPassword()).isNotEmpty();
	}
	
	@DisplayName("회원가입시 중복 이메일은 가입할 수 없다.")
	@Test
	void signUpWithDupEmail() {
		// given
		User preUser = User.builder().email("testlog@gmail.com").password("1231")
				.name("testlog").build();
		userRepository.save(preUser);
		
		SignUp signUp = SignUp.builder()
				.email("testlog@gmail.com")
				.password("1234").name("testlog").build();
		
		// when.. then
		assertThatThrownBy(() -> authService.signUp(signUp))
				.isInstanceOf(AlreadyExistsException.class)
				.hasMessage("이미 가입된 이메일입니다.");
	}
	
	
}