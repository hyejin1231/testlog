package com.test.testlog.contoller;

import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasRole('ROLE_USER')") // 메서드 시큐리티
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal UserPrincipal userPrincipal)
	{
		return "사용자 페이지 입니다. 🙂";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin")
	public String admin()
	{
		return "관리자 페이지 입니다. 😍";
	}
}
