--  *********************************************************************
--  Update to 'r0.2' Database Script
--  *********************************************************************
--  Change Log: db/changelog/changelog.xml
--  Ran at: 1/26/21, 3:09 PM
--  Against: root@172.20.0.1@jdbc:mysql://mysql-demo.docker:3306/demo?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
--  Liquibase version: 3.10.3
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 1, LOCKEDBY = '172.18.0.1 (172.18.0.1)', LOCKGRANTED = '2021-01-26 15:09:48.272' WHERE ID = 1 AND `LOCKED` = 0;

--  Changeset db/changelog/changelog.xml::2021.01.26-20:07-1::generated
INSERT INTO `role` (`role`, version) VALUES ('SYSTEM', 0);

INSERT INTO `role` (`role`, version) VALUES ('ADMIN', 0);

INSERT INTO `role` (`role`, version) VALUES ('USER', 0);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, `DESCRIPTION`, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('2021.01.26-20:07-1', 'generated', 'db/changelog/changelog.xml', NOW(), 8, '8:5e14e0b393d30b984d655c768d03e994', 'insert tableName=role; insert tableName=role; insert tableName=role', '', 'EXECUTED', NULL, NULL, '3.10.3', NULL);

--  Changeset db/changelog/changelog.xml::@Release 0.2::admin
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, `DESCRIPTION`, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID, TAG) VALUES ('@Release 0.2', 'admin', 'db/changelog/changelog.xml', NOW(), 9, '8:1739814725ce06781d10a718e28ad8fa', 'tagDatabase', '', 'EXECUTED', NULL, NULL, '3.10.3', NULL, 'r0.2');

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

