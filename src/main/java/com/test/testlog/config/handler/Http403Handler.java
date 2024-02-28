package com.test.testlog.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Http403Handler implements AccessDeniedHandler
{
	private final ObjectMapper objectMapper;
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException, ServletException
	{
		log.error("[인증오류] 403 접근할 수 없습니다.");
		
		ErrorResponse errorResponse = ErrorResponse.builder().code("403")
				.message("접근할 수 없습니다.").build();
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		response.setStatus(HttpStatus.FORBIDDEN.value());
		
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}

