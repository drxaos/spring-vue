package com.example.demo.config.db;

import org.hibernate.dialect.MySQL8Dialect;

import java.sql.Types;

public class MySQL8CustomInnoDBDialect extends MySQL8Dialect {
    @Override
    protected void registerVarcharTypes() {
        registerColumnType(Types.VARCHAR, "longtext");
        super.registerVarcharTypes();
    }

    public String getTableTypeString() {
        return " engine=innodb ROW_FORMAT=DYNAMIC";
    }
}
