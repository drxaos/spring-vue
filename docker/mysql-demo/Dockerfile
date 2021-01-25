FROM mysql:8.0
USER 1000:1000

COPY ./mysql-demo/mysqld.cnf /etc/mysql/conf.d/mysqld.cnf

ENV TZ=UTC
ENV MYSQL_DATABASE=_mysql-demo.docker
COPY ./mysql-demo/init.sql /docker-entrypoint-initdb.d
