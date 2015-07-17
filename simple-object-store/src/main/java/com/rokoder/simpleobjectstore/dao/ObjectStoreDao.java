package com.rokoder.simpleobjectstore.dao;

import org.joda.time.LocalDateTime;

public interface ObjectStoreDao {

    public interface ObjectStoreCols {
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

    void createTableSchema();

    void insert(String key, byte[] bytes, LocalDateTime expireTime);

    byte[] fetchBytes(String key, LocalDateTime now);

    void update(String key, byte[] bytes, LocalDateTime expireTime);

    void insertOrUpdate(String key, byte[] bytes, LocalDateTime expireTime);

    void delete(String key);

    void deleteByExpireTime(LocalDateTime expireTime);

    boolean isKeyExists(String key);
}
