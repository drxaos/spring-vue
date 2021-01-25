package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String userName = (String) event.getAuthentication().getPrincipal();
        log.warn("Login failed, username: " + userName);
    }
}
