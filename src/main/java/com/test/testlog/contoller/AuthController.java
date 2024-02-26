package com.test.testlog.contoller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.config.AppConfig;
import com.test.testlog.request.SignUp;
import com.test.testlog.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	private final AuthService authService;
	
	private final AppConfig appConfig;
	
	@PostMapping("/auth/signup")
	public void signup(@RequestBody SignUp signUp)
	{
		authService.signUp(signUp);
	}
}
