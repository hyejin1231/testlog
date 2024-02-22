package com.test.testlog.contoller;

import java.time.Duration;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.request.Login;
import com.test.testlog.response.SessionResponse;
import com.test.testlog.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	
	private final AuthService authService;
	
	// 공용키 생성 -> 키를 매번 다이나믹하게 생성해서 암호화하면 로그인할 때
	private final String KEY = "6/XhJr9v+SVANc/Uj0H8I15S7JY8If0QqpEqFIFFeM8="; // 유출되면 안됨 !!
	
	/**
	 * 로그인 1. 세션토큰 발급 직접 (UUID)
	 * @param login
	 * @return
	 */
	/*	@PostMapping("/auth/login")
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
	 */
	
	/**
	 * 로그인 2. JWT를 이용한 인증
	 * @param login
	 * @return
	 */
	@PostMapping("/auth/login")
	public SessionResponse loginWithJwt(@RequestBody Login login)
	{
		// 1) DB에서 조회해서 로그인
//		String accessToken = authService.signIn(login);
		Long userId = authService.signIn(login);
		
		// 2) JWT
//		SecretKey key = Jwts.SIG.HS256.key().build();
//		byte[] encodedKey = key.getEncoded();
//		String strKey = Base64.getEncoder().encodeToString(encodedKey);
		
		SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
		
		String jws = Jwts.builder().subject(String.valueOf(userId)).signWith(secretKey).compact();
		
		return new SessionResponse(jws);
	}
	
}
