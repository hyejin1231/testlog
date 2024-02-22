package com.test.testlog.config;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.test.testlog.config.data.AppConfig;
import com.test.testlog.config.data.UserSession;
import com.test.testlog.domain.Session;
import com.test.testlog.exception.UnAuthorized;
import com.test.testlog.repository.SessionRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 2024.02.20
 * 섹션6. API 인증 시작 (2)
 * : ArgumentResolver 사용해보기
 */
@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver
{
	private final SessionRepository sessionRepository;
	
	private final AppConfig appConfig;
//	private static final String KEY = "6/XhJr9v+SVANc/Uj0H8I15S7JY8If0QqpEqFIFFeM8="; // 유출되면 안됨 !!
	
	@Override
	public boolean supportsParameter(MethodParameter parameter)
	{
		// 1) 메서드의 파라미터 값으로 UserSession 값이 있는지 확인
		return parameter.getParameterType().equals(UserSession.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception
	{
		// 2) 있다면 request에서 accessToken 값을 가져와 Userssion의 name 값에 넣어줌
		
		/* v1. Header에 생성된 Authorization 에 accessToken 담아서 인증
		 String accessToken = webRequest.getHeader("Authorization"); // getParameter -> getHeader, accessToken -> Authorization
				if (accessToken == null || accessToken.isEmpty()) {
					throw new UnAuthorized();
				}
		 */
		
		/* v2. cookie 인증
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		if (servletRequest == null)
		{
			log.error("servletRequest null");
			throw new UnAuthorized();
		}
		Cookie[] cookies = servletRequest.getCookies();
		if (cookies.length == 0) {
			log.error("쿠키가 없음");
			throw new UnAuthorized();
		}
		
		String accessToken = cookies[0].getValue();
		
		// 데이터베이스 사용자 확인 작업
		Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(UnAuthorized::new);
		 */
		
		String jws = webRequest.getHeader("Authorization");
		if (jws == null || jws.isEmpty()) {
			throw new UnAuthorized();
		}
		
//		SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
		SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appConfig.getSecretKey()));
		
		try {
			Jws<Claims> claimsJws = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(jws);
			
			log.info(">>>>>>>>>>>>>>{}", claimsJws);
			
			String userId = claimsJws.getPayload().getSubject();
			return new UserSession(Long.parseLong(userId));
		} catch (JwtException e) {
			throw new UnAuthorized();
		}
		
//		return new UserSession(session.getUser().getId());
	}
}
