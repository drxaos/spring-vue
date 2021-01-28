package com.example.demo.test;

import com.example.demo.Main;
import com.example.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication(scanBasePackageClasses = Main.class)
@ComponentScan(basePackageClasses = Main.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Main.class))
@Configuration
public class TestMain {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");
        log.info("Timezone: UTC, now: " + DateUtils.now());
    }
}
