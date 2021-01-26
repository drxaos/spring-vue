--  *********************************************************************
--  Update to 'r0.1' Database Script
--  *********************************************************************
--  Change Log: db/changelog/changelog.xml
--  Ran at: 1/26/21, 11:13 AM
--  Against: root@172.20.0.1@jdbc:mysql://mysql-demo.docker:3306/demo?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
--  Liquibase version: 3.10.3
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 1, LOCKEDBY = '172.20.0.1 (172.20.0.1)', LOCKGRANTED = '2021-01-26 11:13:31.839' WHERE ID = 1 AND `LOCKED` = 0;

--  Changeset db/changelog/changelog.xml::1611609826895-1::generated
CREATE TABLE `role` (id BIGINT AUTO_INCREMENT NOT NULL, version INT NOT NULL, `role` VARCHAR(255) NULL, CONSTRAINT PK_ROLE PRIMARY KEY (id), UNIQUE (`role`));

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:b68bdaa3dcd53d8c219ca64328d3341e', ORDEREXECUTED = 8 WHERE ID = '1611609826895-1' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::1611609826895-2::generated
CREATE TABLE user (id BIGINT AUTO_INCREMENT NOT NULL, version INT NOT NULL, username VARCHAR(255) NULL, active BIT(1) NULL, real_name VARCHAR(255) NULL, password VARCHAR(255) NULL, CONSTRAINT PK_USER PRIMARY KEY (id), UNIQUE (username));

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:4b19776852d1af3b3f91290b430243ff', ORDEREXECUTED = 9 WHERE ID = '1611609826895-2' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::1611609826895-3::generated
CREATE TABLE user_role (user_id BIGINT NOT NULL, role_id BIGINT NOT NULL, UNIQUE (role_id));

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:7b4b108593d0b79dc30a3f3d15d0bacc', ORDEREXECUTED = 10 WHERE ID = '1611609826895-3' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::1611609826895-4::generated
CREATE INDEX FK5y7ep09jd0dq8too7mh0uhvey ON user_role(user_id);

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:f45a497aa3ad732d8bb5cd57651b5022', ORDEREXECUTED = 11 WHERE ID = '1611609826895-4' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::1611609826895-5::generated
ALTER TABLE user_role ADD CONSTRAINT FK5y7ep09jd0dq8too7mh0uhvey FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:5d4572f37e8bdfa066737f0b3a124639', ORDEREXECUTED = 12 WHERE ID = '1611609826895-5' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::1611609826895-6::generated
ALTER TABLE user_role ADD CONSTRAINT FKd8ei0wqdm5oee96hk5xgvio4i FOREIGN KEY (role_id) REFERENCES `role` (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

UPDATE DATABASECHANGELOG SET DATEEXECUTED = NOW(), DEPLOYMENT_ID = NULL, EXECTYPE = 'RERAN', MD5SUM = '8:b0078e41aacbf52bb02c08bb073bb798', ORDEREXECUTED = 13 WHERE ID = '1611609826895-6' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Changeset db/changelog/changelog.xml::@Release 0.1::admin
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, `DESCRIPTION`, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('@Release 0.1', 'admin', 'db/changelog/changelog.xml', NOW(), 14, '8:9063d8656c8b81df21805c90e0e00d86', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.10.3', NULL, 'r0.1');

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

