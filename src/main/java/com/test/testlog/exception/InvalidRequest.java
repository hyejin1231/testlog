package com.test.testlog.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InvalidRequest extends TestlogException
{
	private static final String MESSAGE = "잘못된 요청입니다.";
	public String fieldName;
	public String errorMessage;

	public InvalidRequest()
	{
		super(MESSAGE);
	}

	public InvalidRequest(String fieldName, String errorMessage) {
		super(MESSAGE);
		addValidation(fieldName, errorMessage);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
