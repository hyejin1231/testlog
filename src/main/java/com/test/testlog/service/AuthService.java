package com.test.testlog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.testlog.domain.Session;
import com.test.testlog.domain.User;
import com.test.testlog.exception.InvalidSigningInformation;
import com.test.testlog.repository.UserRepository;
import com.test.testlog.request.Login;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService
{
	private final UserRepository userRepository;
	
	
	@Transactional
	public String signIn(Login login)
	{
		// 로그인 처리
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword()).orElseThrow(InvalidSigningInformation::new);
		
		// 로그인 처리 되면 세션 발급
		Session session = user.addSession();
		
		return session.getAccessToken();
	}
}
