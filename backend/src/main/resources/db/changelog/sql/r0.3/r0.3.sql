--  *********************************************************************
--  Update to 'r0.3' Database Script
--  *********************************************************************
--  Change Log: db/changelog/changelog.xml
--  Ran at: 1/26/21, 5:30 PM
--  Against: root@172.20.0.1@jdbc:mysql://mysql-demo.docker:3306/demo?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
--  Liquibase version: 3.10.3
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 1, LOCKEDBY = '172.18.0.1 (172.18.0.1)', LOCKGRANTED = '2021-01-26 17:30:50.825' WHERE ID = 1 AND `LOCKED` = 0;

--  Changeset db/changelog/changelog.xml::2021.01.26-22:09-1::admin
--  jdbc sessions
CREATE TABLE SPRING_SESSION (
                PRIMARY_ID CHAR(36) NOT NULL,
                SESSION_ID CHAR(36) NOT NULL,
                CREATION_TIME BIGINT NOT NULL,
                LAST_ACCESS_TIME BIGINT NOT NULL,
                MAX_INACTIVE_INTERVAL INT NOT NULL,
                EXPIRY_TIME BIGINT NOT NULL,
                PRINCIPAL_NAME VARCHAR(100),
                CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
            ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);

CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);

CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
                SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                ATTRIBUTE_BYTES BLOB NOT NULL,
                CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
            ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, `DESCRIPTION`, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('2021.01.26-22:09-1', 'admin', 'db/changelog/changelog.xml', NOW(), 11, '8:eb864d35e581b3274ee0dc4385900df5', 'sql', 'jdbc sessions', 'EXECUTED', NULL, NULL, '3.10.3', NULL);

--  Changeset db/changelog/changelog.xml::@Release 0.3::admin
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, `DESCRIPTION`, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('@Release 0.3', 'admin', 'db/changelog/changelog.xml', NOW(), 12, '8:f7990b441091899d86511bfefce97a67', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.10.3', NULL, 'r0.3');

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

