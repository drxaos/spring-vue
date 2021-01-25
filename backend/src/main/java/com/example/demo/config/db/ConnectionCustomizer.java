package com.example.demo.config.db;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Statement;

@Slf4j
public class ConnectionCustomizer extends AbstractConnectionCustomizer {

    static String instance = "unknown";

    public static void setInstance(String instance) {
        ConnectionCustomizer.instance = instance;
    }

    public void onAcquire(Connection c, String parentDataSourceIdentityToken) throws Exception {
        try (Statement stmt = c.createStatement()) {
            stmt.executeUpdate("SET @@session.time_zone='+00:00';"); // UTC
            stmt.executeUpdate("SET @app_instance = '" + instance + "';");
        }
    }
}
