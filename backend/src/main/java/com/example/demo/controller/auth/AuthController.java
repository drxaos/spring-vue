package com.example.demo.controller.auth;

import com.example.demo.config.app.Cfg;
import com.example.demo.config.app.Config;
import com.example.demo.db.model.auth.User;
import com.example.demo.security.LoginResponse;
import com.example.demo.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    public final Config config;
    public final UserService userService;

    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("login", Map.of("label", config.getString(Cfg.app.title)));
    }

    @GetMapping(value = "/whoami")
    @ResponseBody
    public LoginResponse whoami() {
        User currentUser = userService.getCurrentUser();
        return new LoginResponse(currentUser != null, currentUser != null ? currentUser.getUsername() : null, null);
    }
}
