package com.test.testlog.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * PostEditor를 사용하는 이유
 * 1. 먼저 Post에서 그냥 파라미터로 수정할 값을 받아서 변경해주는 것은 ...
 * 지금은 파라미터가 적으니 가능하지만 이게 늘어나면 헷갈려지고, 잘못 값을 입력하는 경우가 많아지기 때문이다.
 */
@Getter
public class PostEditor
{
	private String title;
	private String content;
	
	public PostEditor(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
	
	public static PostEditorBuilder builder() {
		return new PostEditorBuilder();
	}
	
	public static class PostEditorBuilder {
		private String title;
		private String content;
		
		PostEditorBuilder() {
		}
		
		public PostEditorBuilder title(final String title) {
			if (title != null) {
				this.title = title;
			}
			return this;
		}
		
		public PostEditorBuilder content(final String content) {
			if (content != null) {
				this.content = content;
			}
			return this;
		}
		
		public PostEditor build() {
			return new PostEditor(this.title, this.content);
		}
		
		public String toString() {
			return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
		}
	}
}
