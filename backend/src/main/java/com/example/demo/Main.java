package com.example.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class Main implements ApplicationListener<ContextRefreshedEvent> {

    public static ConfigurableApplicationContext context;

    @Autowired
    Environment env;

    @Override
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());

        log.info("=======================================");
        log.info("  profiles: {}", "" + profiles);
        log.info("=======================================");

        if (profiles.size() != 1) {
            throw new IllegalStateException("wrong profiles: " + profiles);
        }

        if (profiles.contains("dev")) {
            var port = env.getProperty("server.port");

            InetAddress ip = InetAddress.getLocalHost();
            String hostname = ip.getHostName();

            log.info("http://" + hostname + ":" + port + "/");
        }
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");

        SpringApplication app = new SpringApplication(Main.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        context = app.run(args);
    }

}
