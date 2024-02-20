package com.test.testlog.contoller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.domain.User;
import com.test.testlog.exception.InvalidSigningInformation;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.Login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	
	private final UserRepository userRepository;
	
	@PostMapping("/auth/login")
	public User login(@RequestBody Login login)
	{
		// 1) json 으로 아이디/비밀번호
		log.info(">>> login = {}", login);
		
		// 2) DB에서 조회
		User user = userRepository.findByEmailAndPassword(
						login.getEmail(), login.getPassword()).orElseThrow(InvalidSigningInformation::new);
		
		// 3) token 응답
		return user;
	}
	
}
