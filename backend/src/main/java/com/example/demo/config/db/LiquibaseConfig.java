package com.example.demo.config.db;

import com.example.demo.db.repository.PersistableRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Profile("liquibase")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = PersistableRepository.class)
public class LiquibaseConfig {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.liquibaseDiffDataSource.url}")
    String url2;
    @Value("${spring.liquibaseDiffDataSource.username}")
    String username2;
    @Value("${spring.liquibaseDiffDataSource.password}")
    String password2;

    @Value("${spring.liquibaseTestDataSource.url}")
    String url3;
    @Value("${spring.liquibaseTestDataSource.username}")
    String username3;
    @Value("${spring.liquibaseTestDataSource.password}")
    String password3;

    @Bean
    @Primary
    @Profile("liquibase")
    public DataSource referenceDataSource() throws SQLException {
        return getDataSource(url, username, password, true);
    }

    @Bean
    @Profile("liquibase")
    public DataSource targetDataSource() throws SQLException {
        return getDataSource(url2, username2, password2, false);
    }

    @Bean
    @Profile("liquibase")
    public DataSource testDataSource() throws SQLException {
        return getDataSource(url3, username3, password3, false);
    }

    private DataSource getDataSource(String url, String username, String password, boolean clear) throws SQLException {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(com.mysql.cj.jdbc.Driver.class.getCanonicalName())
                .url(url)
                .username(username)
                .password(password)
                .build();

        if (clear) {
            String dbName = dataSource.getConnection().getCatalog();
            try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
                statement.executeUpdate("drop database " + dbName + ";");
                statement.executeUpdate("create database " + dbName + ";");
                statement.executeUpdate("use " + dbName + ";");
            }
        }

        // проверяем
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeQuery("select 1");
        }

        return dataSource;
    }
}
