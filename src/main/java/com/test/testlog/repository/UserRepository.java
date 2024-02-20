package com.test.testlog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.test.testlog.domain.User;

public interface UserRepository extends CrudRepository<User, Long>
{
	Optional<User> findByEmailAndPassword(String email, String password);
}
