package com.example.demo.db.liquibase;

import com.example.demo.db.repository.UserRepository;
import com.example.demo.util.DateUtils;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeLogChild;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.RanChangeSet;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.parser.core.ParsedNodeException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.LiquibaseSerializable;
import liquibase.serializer.core.json.JsonChangeLogSerializer;
import liquibase.serializer.core.string.StringChangeLogSerializer;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Qualifier("testDataSource")
    public DataSource testDataSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public SpringLiquibase springLiquibase;

    @Value("${spring.liquibase.exec}")
    String exec;

    @Profile("liquibase")
    @PostConstruct
    public void liquibaseDiff() throws SQLException, LiquibaseException, IOException, ParserConfigurationException {
        log.info("Liquibase: " + exec);

        if ("diff".equals(exec)) {

            try (Connection connection1 = referenceDataSource.getConnection();
                 Connection connection2 = targetDataSource.getConnection()) {

                MySQLDatabase referenceDatabase = new MySQLDatabase();
                referenceDatabase.setConnection(new JdbcConnection(connection1));

                MySQLDatabase targetDatabase = new MySQLDatabase();
                targetDatabase.setConnection(new JdbcConnection(connection2));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(this.getClass().getClassLoader()), referenceDatabase);
                DiffResult diffResult = liquibase.diff(referenceDatabase, targetDatabase, CompareControl.STANDARD);

                StringBuilder newIgnores = new StringBuilder().append("\n\nignores file:\n");
                List<String> ignores = Files.lines(Paths.get("src/main/resources/db/changelog/ignores")).collect(Collectors.toList());
                JsonChangeLogSerializer ignoreSerializer = new JsonChangeLogSerializer();
                ChangeLogSerializer changeLogSerializer = new XMLChangeLogSerializer() {
                    @Override
                    public <T extends ChangeLogChild> void write(List<T> children, OutputStream out) throws IOException {
                        children.removeIf(c -> {
                            String changes = ("" + ((Collection) c.getSerializableFieldValue("changes")).stream()
                                    .map(change -> "" + ignoreSerializer.serialize((LiquibaseSerializable) change, false))
                                    .collect(Collectors.joining(",")))
                                    .replaceAll("[\\s]", "");
                            if (ignores.contains(changes)) {
                                return true;
                            } else {
                                newIgnores.append(changes).append("\n");
                                return false;
                            }
                        });
                        super.write(children, out);

                        System.out.println(newIgnores + "\n\n");
                    }
                };
                DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false, null);
                DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, diffOutputControl);
                diffToChangeLog.setIdRoot(DateUtils.formatDate(DateUtils.now(), "yyyy.MM.dd-HH:mm"));
                diffToChangeLog.setChangeSetAuthor(System.getProperty("user.name"));
                diffToChangeLog.print("src/main/resources/db/changelog/diff-" + DateUtils.formatDate(DateUtils.now(), "yyyy-MM-dd-HH-mm-ss") + ".xml", changeLogSerializer);
            }

        } else if ("update".equals(exec) || "updateTest".equals(exec)) {
            DataSource updateDataSource = "update".equals(exec) ? targetDataSource : testDataSource;

            try (Connection connection = updateDataSource.getConnection()) {

                MySQLDatabase database = new MySQLDatabase();
                database.setConnection(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(), database);
                liquibase.update("");
            }

        } else if ("sql".equals(exec)) {

            try (Connection connection = targetDataSource.getConnection()) {

                MySQLDatabase database = new MySQLDatabase() {
                    @Override
                    public List<RanChangeSet> getRanChangeSetList() throws DatabaseException {
                        return List.of();
                    }
                };
                database.setOutputDefaultCatalog(false);
                database.setOutputDefaultSchema(false);
                database.setConnection(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(springLiquibase.getChangeLog().replaceFirst("^classpath:", ""), new ClassLoaderResourceAccessor(), database);

                // выделяем последний релиз
                ArrayList<ChangeSet> retain = new ArrayList<>();
                String fromTag = "";
                String nextTag = "";
                for (ChangeSet changeSet : liquibase.getDatabaseChangeLog().getChangeSets()) {
                    if (changeSet.getId().startsWith("@Release")) {
                        TagDatabaseChange tagDatabaseChange = (TagDatabaseChange) changeSet.getChanges().get(0);
                        nextTag = tagDatabaseChange.getTag();
                    } else if (!nextTag.equals(fromTag)) {
                        fromTag = nextTag;
                        retain.clear();
                    }

                    retain.add(changeSet);
                }
                liquibase.getDatabaseChangeLog().getChangeSets().retainAll(retain);

                if (nextTag.equals(fromTag)) {
                    throw new IllegalStateException("no new release at the end of changelog!");
                }

                String dbName = "" + liquibase.getDatabaseChangeLog().getChangeLogParameters().getValue("database.liquibaseSchemaName", null);

                BufferedWriter outputWriter1 = updateOutputWriter(nextTag, dbName, ".sql");
                liquibase.update(nextTag, new Contexts(), new LabelExpression(), outputWriter1);

                BufferedWriter outputWriter2 = updateOutputWriter(nextTag, dbName, ".rollback.sql");
                liquibase.futureRollbackSQL(nextTag, new Contexts(), new LabelExpression(), outputWriter2);
            }
        } else {
            log.error("unknown liquibase command");
        }

        new Thread(() -> System.exit(0)).start();
    }

    private BufferedWriter updateOutputWriter(String nextTag, String dbName, String ext) throws LiquibaseException {
        try {

            File updateSqlOutputFile = new File(new File("src/main/resources/db/changelog/sql", nextTag), nextTag + ext);

            if (!updateSqlOutputFile.exists()) {
                updateSqlOutputFile.getParentFile().mkdirs();
                if (!updateSqlOutputFile.createNewFile()) {
                    throw new LiquibaseException("Cannot create the migration SQL file; " + updateSqlOutputFile.getAbsolutePath());
                }
            }

            log.info("Output SQL Update File: " + updateSqlOutputFile.getAbsolutePath());
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(updateSqlOutputFile), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new LiquibaseException("Failed to create SQL output writer", e);
        }
    }
}
