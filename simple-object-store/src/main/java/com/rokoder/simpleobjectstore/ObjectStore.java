package com.rokoder.simpleobjectstore;

import org.joda.time.LocalDateTime;

public interface ObjectStore {

    String getName();

    void put(String key, Object valueObj);

    void put(String key, Object valueObj, LocalDateTime expireTime);

    void putOrUpdate(String key, Object valueObj);

    void putOrUpdate(String key, Object valueObj, LocalDateTime expireTime);

    <T> T get(String key, Class<T> classType);

    void delete(String key);

    void deleteExpiredObjects();
}
