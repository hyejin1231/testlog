package com.test.testlog.contoller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testlog.domain.User;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.SignUp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest
{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp()
	{
		userRepository.deleteAll();
	}
	
	@DisplayName("회원")
	@Test
	void signUp() throws Exception
	{
		// given
		SignUp signUp = SignUp.builder()
				.email("testlog@gmail.com")
				.password("1234").name("testlog").build();
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(signUp)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
}