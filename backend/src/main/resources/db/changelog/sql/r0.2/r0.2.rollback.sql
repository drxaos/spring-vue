--  *********************************************************************
--  SQL to roll back currently unexecuted changes
--  *********************************************************************
--  Change Log: db/changelog/changelog.xml
--  Ran at: 1/26/21, 3:09 PM
--  Against: root@172.20.0.1@jdbc:mysql://mysql-demo.docker:3306/demo?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
--  Liquibase version: 3.10.3
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 1, LOCKEDBY = '172.18.0.1 (172.18.0.1)', LOCKGRANTED = '2021-01-26 15:09:48.306' WHERE ID = 1 AND `LOCKED` = 0;

--  Rolling Back ChangeSet: db/changelog/changelog.xml::@Release 0.2::admin
DELETE FROM DATABASECHANGELOG WHERE ID = '@Release 0.2' AND AUTHOR = 'admin' AND FILENAME = 'db/changelog/changelog.xml';

--  Rolling Back ChangeSet: db/changelog/changelog.xml::2021.01.26-20:07-1::generated
DELETE FROM `role` WHERE role = 'SYSTEM';

DELETE FROM `role` WHERE role = 'ADMIN';

DELETE FROM `role` WHERE role = 'USER';

DELETE FROM DATABASECHANGELOG WHERE ID = '2021.01.26-20:07-1' AND AUTHOR = 'generated' AND FILENAME = 'db/changelog/changelog.xml';

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET `LOCKED` = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

