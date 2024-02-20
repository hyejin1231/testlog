package com.test.testlog.exception;

public class InvalidSigningInformation extends TestlogException
{
	private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";
	
	public InvalidSigningInformation()
	{
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode()
	{
		return 400;
	}
}
