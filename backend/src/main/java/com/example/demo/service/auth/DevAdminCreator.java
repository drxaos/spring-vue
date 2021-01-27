package com.example.demo.service.auth;

import com.example.demo.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevAdminCreator {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostConstruct
    public void createAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userService.register("admin", "admin", "admin", "SYSTEM");

            log.info("=======================================");
            log.info("REGISTERED admin/admin with SYSTEM role");
            log.info("=======================================");
        }
    }
}
