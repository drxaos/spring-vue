package com.example.demo.util;

import com.example.demo.db.model.auth.User;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.service.auth.UserService;
import com.example.demo.test.AbstractIntegrationTest;
import com.example.demo.test.AbstractUnitTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Optional;

class DateUtilsUnitTest extends AbstractUnitTest {

    @Test
    void register() {
        Assertions.assertThat(DateUtils.formatDateTime(DateUtils.parseDate("2021-02-03 04:05:06")))
                .isEqualTo("04:05:06 03.02.2021");
    }
}
