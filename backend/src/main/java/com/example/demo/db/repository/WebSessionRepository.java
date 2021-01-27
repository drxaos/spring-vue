package com.example.demo.db.repository;

import com.example.demo.db.model.auth.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface WebSessionRepository extends Repository<User, Long> {

    @Query(value = "select count(*) from `SPRING_SESSION`", nativeQuery = true)
    int countSessions();

    @Modifying
    @Transactional
    @Query(value = "delete from `SPRING_SESSION` where PRINCIPAL_NAME = :username", nativeQuery = true)
    int dropSessions(String username);

}
