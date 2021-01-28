package com.example.demo.config.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class ConfigHolder {

    @Autowired
    Environment environment;

    @Autowired
    Config config;

    static ConfigHolder INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static Config get() {
        return INSTANCE.config;
    }

    public static Environment env() {
        return INSTANCE.environment;
    }

}
