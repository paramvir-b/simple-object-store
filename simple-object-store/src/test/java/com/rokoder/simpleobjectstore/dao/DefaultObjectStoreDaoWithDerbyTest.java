package com.rokoder.simpleobjectstore.dao;

import com.rokoder.simpleobjectstore.util.PooledDataSourceFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import javax.sql.DataSource;

public class DefaultObjectStoreDaoWithDerbyTest extends DefaultObjectStoreDaoTest {

    @Override
    public DataSource createDataSource() {
        HikariDataSource dataSource = PooledDataSourceFactory.createDefaultInMemoryDataSourceUsingDerby();

        return dataSource;
    }
}
