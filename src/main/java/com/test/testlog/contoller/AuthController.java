package com.test.testlog.contoller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.request.Login;
import com.test.testlog.response.SessionResponse;
import com.test.testlog.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	
	private final AuthService authService;
	
	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody Login login)
	{
		// 1) json 으로 아이디/비밀번호
		log.info(">>> login = {}", login);
		
		// 2) DB에서 조회
		String accessToken = authService.signIn(login);
		
		// 3) token 응답
		return new SessionResponse(accessToken);
	}
	
}
