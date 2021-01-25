package com.example.demo.service.auth;

import com.example.demo.db.model.auth.User;
import com.example.demo.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;

    private UserDetails getUserDetailsFromSecurityContext() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * достает юзера из спринг-секьюрити и апдейтит его из базы, чтобы проиницилизировались поля
     */
    public User getCurrentUser() {
        return userRepository.findByUsername(getUserDetailsFromSecurityContext().getUsername()).get();
    }
}
