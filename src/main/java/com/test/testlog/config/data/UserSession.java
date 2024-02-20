package com.test.testlog.config.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSession
{
	private final Long id;
	
	public UserSession(Long id)
	{
		this.id = id;
	}
}
