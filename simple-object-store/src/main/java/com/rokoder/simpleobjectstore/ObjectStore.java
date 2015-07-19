package com.rokoder.simpleobjectstore;

import org.joda.time.LocalDateTime;

public interface ObjectStore {

    /**
     * Logical name
     *
     * @return Logical string name for the object store
     */
    String getName();

    /**
     * Unique ID
     *
     * @return Unique id given to the object store.
     */
    String getId();

    /**
     * Insert the object. It does not do any duplication check and allows multiple entries with same key.
     *
     * @param key      Key of the object to be inserted
     * @param valueObj Object to be inserted
     */
    void put(String key, Object valueObj);

    /**
     * Insert the object. It does not do any duplication check and allows multiple entries with same key.
     *
     * @param key        Key of the object to be inserted
     * @param valueObj   Object to be inserted
     * @param expireTime Expiry time when it will be treated as expired
     */
    void put(String key, Object valueObj, LocalDateTime expireTime);

    /**
     * Updates object if given key found else inserts the data.
     *
     * @param key      Key of the object
     * @param valueObj Object to be stored
     */
    void putOrUpdate(String key, Object valueObj);

    /**
     * Updates object if given key found else inserts the data.
     *
     * @param key        Key of the object
     * @param valueObj   Object to be stored
     * @param expireTime Expiry time when it will be treated as expired
     */
    void putOrUpdate(String key, Object valueObj, LocalDateTime expireTime);

    /**
     * Get object for a given key. If the object expiry time in database is less than current system time then null
     * object is returned, and we assume the object is expired.
     *
     * @param key       Key of the object
     * @param classType Class type of the object to be returned
     * @param <T>       Type of object to be returned
     * @return Object with the given key from object store
     */
    <T> T get(String key, Class<T> classType);

    /**
     * Deletes object for a given key. It should delete even duplicated objects for a given key.
     *
     * @param key Key of the object
     */
    void delete(String key);

}
