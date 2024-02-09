package com.test.testlog.exception;

/**
 * 게시글이 존재하지 않을 때 비지니스에 따른 Exception 따로 만들어서 분리
 * status -> 404
 */
public class PostNotFound extends TestlogException
{
	private static final String MESSAGE = "존재하지 않는 글입니다.";
	
	public PostNotFound()
	{
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
