package com.test.testlog.service;

import java.util.Optional;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.testlog.domain.Session;
import com.test.testlog.domain.User;
import com.test.testlog.exception.AlreadyExistsException;
import com.test.testlog.exception.InvalidRequest;
import com.test.testlog.exception.InvalidSigningInformation;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.Login;
import com.test.testlog.request.SignUp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService
{
	private final UserRepository userRepository;
	
	
	@Transactional
	public Long signIn(Login login)
	{
		// 로그인 처리
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword()).orElseThrow(InvalidSigningInformation::new);
		
		// 로그인 처리 되면 세션 발급
		Session session = user.addSession();
		
//		return session.getAccessToken();
		return user.getId();
	}
	
	public void signUp(SignUp signUp)
	{
		// 1) 이메일 중복 확인
		Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
		if (userOptional.isPresent()) {
			throw new AlreadyExistsException();
		}
		
		// 2) 비밀번호 암호화
		SCryptPasswordEncoder passwordEncoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
		String encryptedPassword = passwordEncoder.encode(signUp.getPassword());
		
		User user = User.builder().email(signUp.getEmail())
				.name(signUp.getName()).password(encryptedPassword).build();
		userRepository.save(user);
	}
}
