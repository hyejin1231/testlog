package com.test.testlog.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.test.testlog.config.data.UserSession;
import com.test.testlog.exception.UnAuthorized;

/**
 * 2024.02.20
 * 섹션6. API 인증 시작 (2)
 * : ArgumentResolver 사용해보기
 */
public class AuthResolver implements HandlerMethodArgumentResolver
{
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
		String accessToken = webRequest.getHeader("Authorization"); // getParameter -> getHeader, accessToken -> Authorization
		if (accessToken == null || accessToken.isEmpty()) {
			throw new UnAuthorized();
		}
		
		// 데이터베이스 사용자 확인 작업
		// ...
		
		return new UserSession(1L);
	}
}
