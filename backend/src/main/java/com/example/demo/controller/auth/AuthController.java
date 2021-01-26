package com.example.demo.controller.auth;

import com.example.demo.db.model.auth.User;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.security.LoginResponse;
import com.example.demo.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    public final UserService userService;

    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping(value = "/whoami")
    @ResponseBody
    public LoginResponse whoami() {
        User currentUser = userService.getCurrentUser();
        return new LoginResponse(currentUser != null, currentUser != null ? currentUser.getUsername() : null, null);
    }
}
