package com.example.demo.test;

import com.example.demo.Main;
import com.example.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication(scanBasePackageClasses = Main.class)
@Configuration
public class TestMain {

}
