package com.example.demo.db.liquibase;

import com.example.demo.db.repository.UserRepository;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSetStatus;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Profile("liquibase")
public class LiquibaseMain {

    @Autowired
    Environment env;

    @Autowired
    @Qualifier("referenceDataSource")
    public DataSource referenceDataSource;

    @Autowired
    @Qualifier("targetDataSource")
    public DataSource targetDataSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public SpringLiquibase springLiquibase;

    @Value("${spring.liquibase.exec}")
    String exec;

    @Profile("liquibase")
    @PostConstruct
    public void liquibaseDiff() throws SQLException, LiquibaseException, IOException, ParserConfigurationException {
        if ("diff".equals(exec)) {
            log.info("Liquibase: diff");
            try (Connection connection1 = referenceDataSource.getConnection();
                 Connection connection2 = targetDataSource.getConnection()) {

                MySQLDatabase referenceDatabase = new MySQLDatabase();
                referenceDatabase.setConnection(new JdbcConnection(connection1));

                MySQLDatabase targetDatabase = new MySQLDatabase();
                targetDatabase.setConnection(new JdbcConnection(connection2));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(this.getClass().getClassLoader()), referenceDatabase);
                DiffResult diffResult = liquibase.diff(referenceDatabase, targetDatabase, CompareControl.STANDARD);

                ChangeLogSerializer changeLogSerializer = new XMLChangeLogSerializer();
                DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false, null);
                DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, diffOutputControl);
                diffToChangeLog.print("src/main/resources/db/changelog/diff-" + new Date() + ".xml", changeLogSerializer);
            }

        } else if ("update".equals(exec)) {

            log.info("Liquibase: update");
            try (Connection connection = targetDataSource.getConnection()) {

                MySQLDatabase database = new MySQLDatabase();
                database.setConnection(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(), database);
                liquibase.update("");
            }

        } else if ("sql".equals(exec)) {

            log.info("Liquibase: sql");
            try (Connection connection = targetDataSource.getConnection()) {

                MySQLDatabase database = new MySQLDatabase();
                database.setConnection(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(), database);
                List<ChangeSetStatus> statuses = liquibase.getChangeSetStatuses(new Contexts(), new LabelExpression());

                System.out.println(statuses);

                // todo
            }
        }

        new Thread(() -> System.exit(0)).start();
    }
}
