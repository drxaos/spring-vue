package com.example.demo.job;

import com.example.demo.db.repository.WebSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestJob {

    public final WebSessionRepository webSessionRepository;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void run() {
        log.info("Hello from job! Active sessions: " + webSessionRepository.countSessions());
    }

}
