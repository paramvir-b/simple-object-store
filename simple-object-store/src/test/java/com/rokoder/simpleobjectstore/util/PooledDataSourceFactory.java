package com.rokoder.simpleobjectstore.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.UUID;

public class PooledDataSourceFactory {

    public static HikariDataSource createDefaultInMemoryDataSourceUsingDerby() {


        // Generate unique name for the in-memory data base
        String id = UUID.randomUUID().toString();
        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
//        config.setDataSourceClassName("org.apache.derby.jdbc.EmbeddedDataSource");
        config.setJdbcUrl("jdbc:derby:memory:" + id + ";create=true");

//
//        cpds.setMinPoolSize(5);
//        cpds.setAcquireIncrement(5);
//        cpds.setMaxPoolSize(20);
//
        return new HikariDataSource(config);
    }

}
