package com.test.testlog.service;

import java.util.Optional;

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
	
	public void signUp(SignUp signUp)
	{
		// 1) 이메일 중복 확인
		Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
		if (userOptional.isPresent()) {
			throw new AlreadyExistsException();
		}
		
		User user = User.builder().email(signUp.getEmail())
				.name(signUp.getName()).password(signUp.getPassword()).build();
		userRepository.save(user);
	}
}
