package com.example.demo.test;

import com.example.demo.config.app.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.support.TestPropertySourceUtils;

import javax.persistence.EntityManager;
import java.io.IOException;

@ActiveProfiles("test")
@SpringBootTest(classes = TestMain.class, properties = "spring.application.admin.enabled=false")
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Autowired
    private Config config;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void configBefore() throws IOException {
        TestPropertySourceUtils.convertInlinedPropertiesToMap();
        savedConfig = config.exportConfig();
    }

    @AfterEach
    public void configAfter() throws IOException {
        config.importConfig(savedConfig);
    }

    public void flushDb() {
        entityManager.flush();
        entityManager.clear();
    }

}
