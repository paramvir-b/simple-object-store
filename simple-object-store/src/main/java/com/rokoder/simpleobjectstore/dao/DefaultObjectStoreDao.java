package com.rokoder.simpleobjectstore.dao;

import com.rokoder.simpleobjectstore.util.DatabaseUtil;
import com.rokoder.simpleobjectstore.util.StringUtil;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

/**
 * We created DAO so that in future we can implement for various Databases. And generally these databases differ slightly
 */
public class DefaultObjectStoreDao implements ObjectStoreDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultObjectStoreDao.class);
    private final String selectQueryWithStringKeyStr;
    private final String queryUpdateKey;
    private final String queryAllKeyCount;
    private final String queryDeleteKey;
    private final String queryDeleteByExpireTime;

    /**
     * Data source used as storage
     */
    private final DataSource dataSource;
    /**
     * Table used to insert key/value
     */
    private final String tableName;

    public DefaultObjectStoreDao(DataSource dataSource, String tableName) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource cannot be null");
        }

        if (tableName == null) {
            throw new IllegalArgumentException("Table name is null");
        }

        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is empty");
        }

        if (tableName.contains(" ")) {
            throw new IllegalArgumentException("Table name cannot contain space. tableName='" + tableName + "'");
        }

        this.dataSource = dataSource;
        this.tableName = tableName;

        selectQueryWithStringKeyStr = "SELECT " +
                ObjectStoreCols.VALUE_OBJ +
                " FROM " +
                tableName +
                " where " +
                ObjectStoreCols.KEY + "=? and (" +
                ObjectStoreCols.EXPIRE_TIME + ">=? or " +
                ObjectStoreCols.EXPIRE_TIME + " IS NULL )";
        queryUpdateKey = DatabaseUtil.createUpdateQuery(tableName,
                new String[]{ObjectStoreCols.UPDATE_TIME, ObjectStoreCols.EXPIRE_TIME, ObjectStoreCols.VALUE_OBJ}) +
                " where " + ObjectStoreCols.KEY + "=?";


        queryAllKeyCount = "SELECT count(*) FROM " +
                tableName +
                " where " +
                ObjectStoreCols.KEY + "=?";

        queryDeleteKey = "delete from " + tableName + " where " + ObjectStoreCols.KEY + "=?";
        queryDeleteByExpireTime = "delete from " + tableName + " where " + ObjectStoreCols.EXPIRE_TIME + "<?";
    }

    @Override
    public void createTableSchema() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();

            String query = "create table " + tableName + " (" +
                    ObjectStoreCols.CREATE_TIME + " TIMESTAMP, " +
                    ObjectStoreCols.UPDATE_TIME + " TIMESTAMP, " +
                    ObjectStoreCols.EXPIRE_TIME + " TIMESTAMP, " +
                    ObjectStoreCols.KEY + " VARCHAR(200), " +
                    ObjectStoreCols.VALUE_OBJ + " BLOB" + ")";
            LOGGER.info("create table query={}", query);
            stmt.execute(query);

            // Create index for better performance of fetching data
            query = "CREATE index " + tableName + "_" + ObjectStoreCols.KEY +
                    " on " + tableName + " (" + ObjectStoreCols.KEY + ")";
            LOGGER.info("create index query={}", query);
            stmt.execute(query);
        } catch (SQLException e) {
            throw new IllegalStateException("Creating table failed for: " + e.getMessage(), e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, null);
        }
    }

    @Override
    public void insert(String key, byte[] bytes, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Storing bytes stringKey={}  expireTime={} \nbytes({})={}", key, expireTime, bytes.length,
                    StringUtil.byteArrayToHexString(bytes));
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            String insertQueryStr;

            insertQueryStr = DatabaseUtil.createInsertQuery(tableName,
                    new String[]{ObjectStoreCols.CREATE_TIME, ObjectStoreCols.UPDATE_TIME, ObjectStoreCols.EXPIRE_TIME, ObjectStoreCols.KEY, ObjectStoreCols.VALUE_OBJ});

            stmt = conn.prepareStatement(insertQueryStr);
            LocalDateTime ldt = new LocalDateTime();
            Timestamp createTimeStamp = new Timestamp(ldt.toDate().getTime());
            stmt.setTimestamp(1, createTimeStamp);
            stmt.setTimestamp(2, createTimeStamp);
            if (expireTime != null) {
                stmt.setTimestamp(3, new Timestamp(expireTime.toDate().getTime()));
            } else {
                stmt.setTimestamp(3, null);
            }
            stmt.setString(4, key);
            stmt.setBytes(5, bytes);
            stmt.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Storing stringKey=" + key + " to  table" + tableName + " failed with error: " + e.getMessage(), e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, null);
        }
    }

    @Override
    public byte[] fetchBytes(String key, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Fetching bytes stringKey={} expireTime={}", key, expireTime);
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        byte[] bytes = null;
        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(selectQueryWithStringKeyStr);
            stmt.setString(1, key);

            Timestamp nowTS = new Timestamp(expireTime.toDate().getTime());
            stmt.setTimestamp(2, nowTS);

            rs = stmt.executeQuery();
            if (rs.next()) {
                bytes = rs.getBytes(1);
                if (rs.next()) {
                    throw new IllegalStateException("Key should return only one row");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Fetching stringKey=" + key + " from table=" + tableName + " failed with error: " + e.getMessage(),
                    e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, rs);
        }

        return bytes;
    }

    @Override
    public void update(String key, byte[] bytes, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Updating bytes stringKey={}  expireTime={} \nbytes({})={}", key, expireTime, bytes.length,
                    StringUtil.byteArrayToHexString(bytes));
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(queryUpdateKey);
            LocalDateTime ldt = new LocalDateTime();
            Timestamp updateTime = new Timestamp(ldt.toDate().getTime());
            stmt.setTimestamp(1, updateTime);
            if (expireTime != null) {
                stmt.setTimestamp(2, new Timestamp(expireTime.toDate().getTime()));
            } else {
                stmt.setTimestamp(2, null);
            }
            stmt.setBytes(3, bytes);
            stmt.setString(4, key);
            stmt.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Updating stringKey=" + key + " to  table" + tableName + " failed with error: " + e.getMessage(),
                    e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, null);
        }

    }

    @Override
    public void insertOrUpdate(String key, byte[] bytes, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("insertOrUpdate stringKey={}", key);
        }

        long keyCount = getAllKeyCount(key);
        if (keyCount == 1) {
            // Update the data
            update(key, bytes, expireTime);
        } else if (keyCount == 0) {
            insert(key, bytes, expireTime);
        } else {
            throw new IllegalArgumentException(
                    "Cannot update kye as more than one found keyCount=" + keyCount + ". Store or update only works if we have unique keys");
        }
    }

    @Override
    public void delete(String key) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(queryDeleteKey);
            stmt.setString(1, key);
            stmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Fetching meta data longKey=" + key + " from table=" + tableName + " failed with error: " + e.getMessage(),
                    e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, null);
        }

    }

    @Override
    public void deleteByExpireTime(LocalDateTime expireTime) {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Deleting expired objects expireTime={}", expireTime);
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(queryDeleteByExpireTime);

            Timestamp delTS = new Timestamp(expireTime.toDate().getTime());

            stmt.setTimestamp(1, delTS);
            stmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Deleting expired objects from table=" + tableName + " failed with error: " + e.getMessage(), e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, null);
        }
    }

    private long getAllKeyCount(String key) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        long keyCount = -1;
        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(queryAllKeyCount);
            stmt.setString(1, key);

            rs = stmt.executeQuery();

            if (rs.next()) {
                keyCount = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Fetching all key count key=" + key + " from table=" + tableName + " failed with error: " + e.getMessage(),
                    e);
        } finally {
            DatabaseUtil.closeConnStmtRsAndIgnoreExp(conn, stmt, rs);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("key={} keyCount={} ={}", key, keyCount);
        }
        return keyCount;
    }

}
