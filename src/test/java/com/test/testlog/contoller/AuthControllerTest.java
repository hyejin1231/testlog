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
import com.test.testlog.domain.Session;
import com.test.testlog.domain.User;
import com.test.testlog.exception.AlreadyExistsException;
import com.test.testlog.repository.SessionRepository;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.Login;
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
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@BeforeEach
	void setUp()
	{
		sessionRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	
	@DisplayName("로그인 성공")
	@Test
	void login() throws Exception
	{
		// given
		User preUser = User.builder().email("testlog@gmail.com").password("1234") // 사실 비밀번호는 평문으로 하는 건 좋지않다.. 암호화 알아보기 ?
				.name("test")
				.build();
		userRepository.save(preUser);
		
		Login login = Login.builder().email("testlog@gmail.com")
				.password("1234").build();
		
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(login)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
//	@Transactional // 지연 로딩 에러를 해결하는 방법 1. -> 근데 나중에 테스트 오염이 될 수 있기 때문에 최선의 방법은 아니라고 한다.
//	@DisplayName("로그인 성공 후 세션 1개 생성 확인 테스트")
//	@Test
	void loginAfterCreateSession() throws Exception
	{
		// given
		User preUser = User.builder().email("testlog@gmail.com").password("1234") // 사실 비밀번호는 평문으로 하는 건 좋지않다.. 암호화 알아보기 ?
				.name("test")
				.build();
		User saveUser = userRepository.save(preUser);
		
		Login login = Login.builder().email("testlog@gmail.com")
				.password("1234").build();
		
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(login)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		
		User loggedInUser = userRepository.findById(saveUser.getId())
				.orElseThrow(RuntimeException::new);
		
		assertThat(loggedInUser.getSessions().size()).isEqualTo(1);
		
	}
	
	@DisplayName("로그인 성공 후 세션 1개 생성 후 세션 응답 테스트")
	@Test
	void loginAfterCreateSession2() throws Exception
	{
		// given
		User preUser = User.builder().email("testlog@gmail.com").password("1234") // 사실 비밀번호는 평문으로 하는 건 좋지않다.. 암호화 알아보기 ?
				.name("test")
				.build();
		User saveUser = userRepository.save(preUser);
		
		Login login = Login.builder().email("testlog@gmail.com")
				.password("1234").build();
		
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(login)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@DisplayName("로그인 성공 후 권한이 필요한 페이지로 접속")
	@Test
	void loginAndDirectByAuthPage() throws Exception
	{
		// given
		User user = User.builder().email("testlog@gmail.com").password("1234") // 사실 비밀번호는 평문으로 하는 건 좋지않다.. 암호화 알아보기 ?
				.name("test")
				.build();
		Session session = user.addSession();
		userRepository.save(user);
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.get("/hello")
								.header("Authorization", session.getAccessToken())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	@DisplayName("로그인 성공 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
	@Test
	void noAuthLoginAndDirectByAuthPage() throws Exception
	{
		// given
		User user = User.builder().email("testlog@gmail.com").password("1234") // 사실 비밀번호는 평문으로 하는 건 좋지않다.. 암호화 알아보기 ?
				.name("test")
				.build();
		Session session = user.addSession();
		userRepository.save(user);
		
		// when.. then
		mockMvc.perform(MockMvcRequestBuilders.get("/hello")
								.header("Authorization", session.getAccessToken() +"-asdf")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andDo(MockMvcResultHandlers.print());
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