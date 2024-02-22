package com.test.testlog.config.data;

import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "testlog")
public class AppConfig
{
	private byte[] secretKey;
	
	public void setSecretKey(String secretKey)
	{
		this.secretKey = Base64.getDecoder().decode(secretKey);
	}
}
