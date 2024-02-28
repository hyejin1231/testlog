package com.test.testlog.contoller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.config.UserPrincipal;

@RestController
public class MainController
{
	@GetMapping("/")
	public String main()
	{
		return "메인 페이지 입니다.";
	}
	
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal UserPrincipal userPrincipal)
	{
		return "사용자 페이지 입니다. 🙂";
	}
	
	@GetMapping("/admin")
	public String admin()
	{
		return "관리자 페이지 입니다. 😍";
	}
}
