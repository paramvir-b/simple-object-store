package com.rokoder.simpleobjectstore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

    private DatabaseUtil() {
        // Util class so no constructor
    }

    /**
     * Helper api to close connection, statement and resultset and ignoring @{link SQLException} as
     * we cannot do much once we have this excepiton while closing
     *
     * @param connection Connection to be closed. If not null it will be closed.
     * @param statement  Statment to be closed. If not null it will be closed.
     * @param resultSet  Result Set to be closed. If not null it will be closed.
     */
    public static void closeConnStmtRsAndIgnoreExp(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Exception occurred while closing. Ignored exception.", e);

        }
    }

    /**
     * @param tableName  Name of table in which to insert
     * @param columnList Array of column names
     * @return Insert query with binding parameters
     */
    public static String createInsertQuery(String tableName, String[] columnList) {
        checkTableName(tableName);
        checkColumnList(columnList);

        StringBuilder sb = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();
        sb.append("insert into ");

        sb.append(tableName);
        sb.append(" (");
        sbValues.append(" values (");

        int columnListLen = columnList.length;
        for (int i = 0; i < columnListLen; i++) {
            sb.append(columnList[i]);
            sbValues.append("?");
            if (i + 1 < columnList.length) {
                sb.append(", ");
                sbValues.append(", ");
            }
        }

        sb.append(")");
        sbValues.append(")");

        sb.append(sbValues);

        LOGGER.trace("insert query={}", sb.toString());
        return sb.toString();
    }

    /**
     * @param tableName  Name of table for update query
     * @param columnList Array of column names
     * @return Update query with binding parameters
     */
    public static String createUpdateQuery(String tableName, String[] columnList) {
        checkTableName(tableName);
        checkColumnList(columnList);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");

        sb.append(tableName);
        sb.append(" set ");

        int columnListLen = columnList.length;
        for (int i = 0; i < columnListLen; i++) {
            sb.append(columnList[i]);
            sb.append("=?");
            if (i + 1 < columnList.length) {
                sb.append(", ");
            }
        }

        LOGGER.trace("udpate query={}", sb.toString());
        return sb.toString();
    }

    private static void checkTableName(String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName is null");
        }

        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("tableName is empty");
        }

        if (tableName.contains(" ")) {
            throw new IllegalArgumentException("Table name cannot contain space. tableName='" + tableName + "'");
        }

    }

    private static void checkColumnList(String[] columnList) {
        if (columnList == null) {
            throw new IllegalArgumentException("columnList cannot be null");
        }
        if (columnList.length == 0) {
            throw new IllegalArgumentException("columnList cannot be of zero length");
        }
    }
}