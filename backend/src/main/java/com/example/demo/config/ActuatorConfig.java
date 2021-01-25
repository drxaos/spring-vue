package com.example.demo.config;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        InMemoryHttpTraceRepository traceRepository = new InMemoryHttpTraceRepository() {
            @Override
            public void add(HttpTrace trace) {
                if (trace.getRequest().getUri().getPath().startsWith("/api/")) {
                    super.add(trace);
                }
            }
        };
        traceRepository.setCapacity(300);
        traceRepository.setReverse(true);
        return traceRepository;
    }

}
