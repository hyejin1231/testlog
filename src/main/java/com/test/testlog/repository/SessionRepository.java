package com.test.testlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.test.testlog.domain.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>
{
}
