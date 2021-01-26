package com.example.demo.service.auth;

import com.example.demo.db.model.auth.User;
import com.example.demo.db.repository.RoleRepository;
import com.example.demo.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private UserDetails getUserDetailsFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            return null;
        }
        return (UserDetails) principal;
    }

    /**
     * достает юзера из спринг-секьюрити и апдейтит его из базы, чтобы проиницилизировались поля
     */
    public User getCurrentUser() {
        UserDetails userDetails = getUserDetailsFromSecurityContext();
        if (userDetails == null) {
            return null;
        }
        return userRepository.findByUsername(userDetails.getUsername()).get();
    }

    public User register(String username, String realName, String password, String role) {
        return userRepository.save(new User(null, 0,
                username,
                passwordEncoder.encode(password),
                realName,
                true,
                List.of(roleRepository.findByRole(role).get())));
    }
}
