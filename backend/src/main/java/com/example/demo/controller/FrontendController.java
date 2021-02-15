package com.example.demo.controller;

import com.example.demo.config.app.Config;
import com.example.demo.service.auth.FrontendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FrontendController {

    public final FrontendService frontendService;
    public final Config config;

    @ResponseBody
    @GetMapping(value = "/index")
    public String exec() throws ScriptException {
        frontendService.exec();
        return "ok";
    }

}
