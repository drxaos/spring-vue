--  *********************************************************************
--  SQL to roll back currently unexecuted changes
--  *********************************************************************
--  Change Log: db/changelog/changelog.xml
--  Ran at: 1/26/21, 11:13 AM
--  Against: root@172.20.0.1@jdbc:mysql://mysql-demo.docker:3306/demo?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
--  Liquibase version: 3.10.3
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 1, LOCKEDBY = '172.20.0.1 (172.20.0.1)', LOCKGRANTED = '2021-01-26 11:13:31.883' WHERE ID = 1 AND `LOCKED` = 0;

--  Rolling Back ChangeSet: db/changelog/changelog.xml::@Release 0.1::admin
DELETE FROM DATABASECHANGELOG WHERE ID = '@Release 0.1' AND AUTHOR = 'admin' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-6::generated
ALTER TABLE user_role DROP FOREIGN KEY FKd8ei0wqdm5oee96hk5xgvio4i;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-6' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-5::generated
ALTER TABLE user_role DROP FOREIGN KEY FK5y7ep09jd0dq8too7mh0uhvey;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-5' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-4::generated
DROP INDEX FK5y7ep09jd0dq8too7mh0uhvey ON user_role;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-4' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-3::generated
DROP TABLE user_role;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-3' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-2::generated
DROP TABLE user;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-2' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::1611609826895-1::generated
DROP TABLE `role`;

DELETE FROM DATABASECHANGELOG WHERE ID = '1611609826895-1' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

