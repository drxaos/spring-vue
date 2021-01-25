package com.example.demo.config.db;

import com.example.demo.db.repository.DbRepository;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Profile("!liquibase")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = DbRepository.class)
public class DbConfig {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    @Value("${app.label}")
    private String label;

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        return getComboPooledDataSource(url, username, password);
    }

    private ComboPooledDataSource getComboPooledDataSource(String url, String username, String password) throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource("dataSource");

        try {
            dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class.getCanonicalName());
        } catch (PropertyVetoException pve) {
            System.out.println("Cannot load datasource driver : " + pve.getMessage());
            return null;
        }
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setInitialPoolSize(10);
        dataSource.setMinPoolSize(10);
        dataSource.setMaxPoolSize(100);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setAcquireIncrement(10);
        dataSource.setMaxIdleTime(30);
        dataSource.setMaxConnectionAge(30);
        dataSource.setIdleConnectionTestPeriod(10);
        ConnectionCustomizer.setInstance(label);
        dataSource.setConnectionCustomizerClassName(ConnectionCustomizer.class.getName());

        // проверяем
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeQuery("select 1");
        }

        return dataSource;
    }


}
