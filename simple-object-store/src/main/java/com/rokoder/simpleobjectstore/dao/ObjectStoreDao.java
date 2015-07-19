package com.rokoder.simpleobjectstore.dao;

import org.joda.time.LocalDateTime;

/**
 * DAO for storing byte arrays. This is kept separate so that we can have different implementations based on the database
 * we are using.
 */
public interface ObjectStoreDao {

    public final static class ObjectStoreCols {
        /**
         * Create time for entry
         */
        public static final String CREATE_TIME = "CREATE_TIME";
        /**
         * Update time for entry
         */
        public static final String UPDATE_TIME = "UPDATE_TIME";
        /**
         * Expire time for entry
         */
        public static final String EXPIRE_TIME = "EXPIRE_TIME";
        /**
         * Key part of the entry
         */
        public static final String KEY = "KEY_STR";
        /**
         * Value part of the entry
         */
        public static final String VALUE_OBJ = "VALUE_OBJ";

    }

    /**
     * Create table schema for dao. This api does not need to guarrantty for already existing table.
     * It should at least throw exception in that case
     */
    void createTableSchema();

    /**
     * Insert the byte array in the database. It does not do any duplication check and allows multiple entries
     * with same key.
     *
     * @param key        Key of the byte array
     * @param bytes      Byte array to be stored
     * @param expireTime Expiry time when it will be treated as expired
     */
    void insert(String key, byte[] bytes, LocalDateTime expireTime);

    /**
     * Fetches byte array for a given key.
     *
     * @param key        Key for the byte array
     * @param expireTime Time to use as expiry time. If the byte array expiry time in database is less than expire time
     *                   passed as argument here then null byte array is returned, and we assume the object is expired.
     * @return byte array for a given key
     */
    byte[] fetchBytes(String key, LocalDateTime expireTime);

    /**
     * Updates byte array for a given key.
     *
     * @param key        Key for the byte array
     * @param bytes      New bytes which will replace old byte array
     * @param expireTime New expire time
     */
    void update(String key, byte[] bytes, LocalDateTime expireTime);

    /**
     * Updates if given key found else inserts the data.
     *
     * @param key        Key for the byte array
     * @param bytes      Byte array to be stored
     * @param expireTime Expiry time when it will be treated as expired
     */
    void insertOrUpdate(String key, byte[] bytes, LocalDateTime expireTime);

    /**
     * Deletes data for a given key. It should delete even duplicated entries for a given key.
     *
     * @param key Key to be deleted
     */
    void delete(String key);

    /**
     * Deletes data whose expire time less than expire time passed
     *
     * @param expireTime Expire time used for deletion.
     */
    void deleteByExpireTime(LocalDateTime expireTime);

}
