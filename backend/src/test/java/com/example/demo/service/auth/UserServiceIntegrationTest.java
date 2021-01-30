package com.example.demo.service.auth;

import com.example.demo.db.model.auth.User;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.test.AbstractIntegrationTest;
import com.example.demo.util.DateUtils;
import com.example.demo.util.TestDateUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Optional;

class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void register() {
        TestDateUtils.mockDateUtilsNow("2021-01-01 01:01:01");

        Assertions.assertThat(userRepository.findByUsername("user1")).isEmpty();

        User registered = userService.register("user1", "userName1", "qwerty", "USER");

        Assertions.assertThat(registered).isNotNull();

        Optional<User> user1 = userRepository.findByUsername("user1");

        Assertions.assertThat(user1)
                .isPresent().get()
                .isEqualTo(registered)
                .extracting(User::getCreated)
                .isEqualTo(DateUtils.parseDate("2021-01-01 01:01:01"));

        Assertions.assertThat(passwordEncoder.matches("qwerty", user1.get().getPassword()))
                .isTrue();
    }
}