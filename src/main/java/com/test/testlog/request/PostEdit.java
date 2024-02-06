package com.test.testlog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 지금은 Post 객체와 내용이 똑같더라도 내용이 다르면 분리해서 사용하는 것이 맞다 !!
 */
@Getter
@Setter
@ToString
public class PostEdit
{
	@NotBlank(message = "title을 입력하세요.")
	private String title;
	
	@NotBlank(message  ="content를 입력하세요.")
	private String content;;
	
	@Builder
	public PostEdit(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
}
