package com.test.testlog.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter
{
	private final ObjectMapper objectMapper;
	public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper)
	{
		super(loginUrl);
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException
	{
		EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
		
		UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
				emailPassword.email, emailPassword.password
		);
		
		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
		
		return this.getAuthenticationManager().authenticate(token);
	}
	
	@Getter
	private static class EmailPassword
	{
		private String email;
		
		private String password;
	}
}
