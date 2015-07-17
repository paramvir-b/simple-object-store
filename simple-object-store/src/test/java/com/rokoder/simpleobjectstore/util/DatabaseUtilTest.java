package com.rokoder.simpleobjectstore.util;

import org.junit.Assert;
import org.junit.Test;
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

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInsertQueryWithNullTableName() {
        try {
            DatabaseUtil.createInsertQuery(null, new String[]{"col1", "col2"});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("tableName is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInsertQueryWithEmptyTableName() {
        try {
            DatabaseUtil.createInsertQuery("", new String[]{"col1", "col2"});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("tableName is empty", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInsertQueryWithSpaceInTableName() {
        try {
            DatabaseUtil.createInsertQuery("table name", new String[]{"col1", "col2"});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Table name cannot contain space. tableName='table name'", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInsertQueryWithNullColumns() {
        try {
            DatabaseUtil.createInsertQuery("table_name", null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("columnList cannot be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInsertQueryWithEmptyColumns() {
        try {
            DatabaseUtil.createInsertQuery("table_name", new String[0]);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("columnList cannot be of zero length", e.getMessage());
            throw e;
        }
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

    @Test
    public void testCloseConnStmtRsAndIgnoreExpWithException() throws SQLException {
        Connection mockConn = Mockito.mock(Connection.class);
        Statement mockStmt = Mockito.mock(Statement.class);
        ResultSet mockRS = Mockito.mock(ResultSet.class);

        Mockito.doThrow(new SQLException()).when(mockConn).close();

        DatabaseUtil.closeConnStmtRsAndIgnoreExp(mockConn, mockStmt, mockRS);

        Mockito.verify(mockConn).close();
        Mockito.verify(mockStmt).close();
        Mockito.verify(mockRS).close();

    }

}
