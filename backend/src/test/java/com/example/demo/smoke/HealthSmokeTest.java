package com.example.demo.smoke;

import com.example.demo.config.app.Config;
import com.example.demo.service.auth.UserService;
import com.example.demo.test.AbstractSmokeTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthSmokeTest extends AbstractSmokeTest {

    @Autowired
    UserService userService;

    @Autowired
    DataSource dataSource;

    @Autowired
    Config config;

    @Test
    public void startupHealth() {

        assertThat(userService).isNotNull();
        assertThat(dataSource).isNotNull();
        assertThat(config).isNotNull();

    }

}
