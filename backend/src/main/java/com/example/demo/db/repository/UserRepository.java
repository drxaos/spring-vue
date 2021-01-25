package com.example.demo.db.repository;

import com.example.demo.db.model.auth.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends DbRepository<User> {
    Optional<User> findByUsername(String username);
}
