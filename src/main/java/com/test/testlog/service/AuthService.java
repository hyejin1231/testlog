package com.test.testlog.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.testlog.domain.User;
import com.test.testlog.exception.AlreadyExistsException;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.SignUp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService
{
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	// end::[]]
	
	public void signUp(SignUp signUp)
	{
		// 1) 이메일 중복 확인
		Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
		if (userOptional.isPresent()) {
			throw new AlreadyExistsException();
		}
		
		String encryptedPassword = passwordEncoder.encode(signUp.getPassword());
		
		User user = User.builder().email(signUp.getEmail())
				.name(signUp.getName()).password(encryptedPassword).build();
		userRepository.save(user);
	}
}
