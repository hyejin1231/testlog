package com.test.testlog.contoller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> login(@RequestBody Login login)
	{
		// 1) json 으로 아이디/비밀번호
		log.info(">>> login = {}", login);
		
		// 2) DB에서 조회
		String accessToken = authService.signIn(login);
		
		ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
				.domain("localhost") // TODO : 서버 환경에 따른 분리 필요
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofDays(30)) // 쿠키 만료 시간 (로그인 유지 시간), 쿠키는 한달이 국룰이라고 함 ㅋ
				.sameSite("Strict") // ? 이건 뭐지?
				.build();
		
		log.info(">>> cookie={}" , cookie);
		
		// 3) token 응답
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
	}
	
}
