package com.rokoder.simpleobjectstore.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtilTest {

    @Test
    public void testCreateInsertQuery() {
        String actInsertQuery = DatabaseUtil.createInsertQuery("table_name", new String[]{"col1", "col2"});

        Assert.assertEquals("insert into table_name (col1, col2) values (?, ?)", actInsertQuery);

    }

    @Test
    public void testCreateUpdateQuery() {
        String actUpdateQuery = DatabaseUtil.createUpdateQuery("table_name", new String[]{"col1", "col2"});

        Assert.assertEquals("update table_name set col1=?, col2=?", actUpdateQuery);
    }

    @Test
    public void testCloseConnStmtRsAndIgnoreExp() throws SQLException {
        Connection mockConn = Mockito.mock(Connection.class);
        Statement mockStmt = Mockito.mock(Statement.class);
        ResultSet mockRS = Mockito.mock(ResultSet.class);

        DatabaseUtil.closeConnStmtRsAndIgnoreExp(mockConn, mockStmt, mockRS);

        Mockito.verify(mockConn).close();
        Mockito.verify(mockStmt).close();
        Mockito.verify(mockRS).close();

    }

}
