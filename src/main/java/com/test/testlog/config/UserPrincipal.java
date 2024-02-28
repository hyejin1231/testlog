package com.test.testlog.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User
{
	
	private final Long userId;
	
	public UserPrincipal(com.test.testlog.domain.User user)
	{
		super(user.getEmail(), user.getPassword(),
			  List.of(
					  new SimpleGrantedAuthority("ROLE_USER")
//					  new SimpleGrantedAuthority("WRITE"))
		)); // ROLE 이 있으면 역할, 없으면 권한
		this.userId = user.getId();
	}
	
	public Long getUserId()
	{
		return userId;
	}
}
