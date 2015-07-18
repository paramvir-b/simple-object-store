package com.rokoder.simpleobjectstore;

import com.rokoder.simpleobjectstore.dao.ObjectStoreDao;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ObjectStoreImpl implements ObjectStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStoreImpl.class);

    /**
     * Unique id to distinguish b/w object stores. Used in logs.
     */
    private final String id = UUID.randomUUID().toString();

    /**
     * Logical name for the object insert to be used in the logs
     */
    private final String name;

    private final ObjectStoreDao objectStoreDao;
    /**
     * Serializer used to convert from object to bytes
     */
    private final ObjectSerializer objectSerializer;

    public ObjectStoreImpl(String name, ObjectStoreDao objectStoreDao, ObjectSerializer objectSerializer) {
        this(name, objectStoreDao, objectSerializer, false);
    }

    public ObjectStoreImpl(String name, ObjectStoreDao objectStoreDao, ObjectSerializer objectSerializer, boolean createSchema) {
        this.name = name;
        this.objectStoreDao = objectStoreDao;
        this.objectSerializer = objectSerializer;

        if (createSchema) {
            objectStoreDao.createTableSchema();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void put(String key, Object valueObj) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("put key={} value={} in object insert id={} name={}", key, valueObj, id, name);
        }
        byte[] bytes = objectSerializer.toBytes(valueObj);
        objectStoreDao.insert(key, bytes, null);
    }

    @Override
    public void put(String key, Object valueObj, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("put key={} value={} in object store id={} name={}", key, valueObj, id, name);
        }
        byte[] bytes = objectSerializer.toBytes(valueObj);
        objectStoreDao.insert(key, bytes, expireTime);
    }

    @Override
    public void putOrUpdate(String key, Object valueObj) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("put key={} value={} in object store id={} name={}", key, valueObj, id, name);
        }
        byte[] bytes = objectSerializer.toBytes(valueObj);
        objectStoreDao.insertOrUpdate(key, bytes, null);
    }

    @Override
    public void putOrUpdate(String key, Object valueObj, LocalDateTime expireTime) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("put key={} value={} in object store id={} name={}", key, valueObj, id, name);
        }
        byte[] bytes = objectSerializer.toBytes(valueObj);
        objectStoreDao.insertOrUpdate(key, bytes, expireTime);
    }

    @Override
    public <T> T get(String key, Class<T> classType) {
        byte[] bytes = objectStoreDao.fetchBytes(key, new LocalDateTime());
        return objectSerializer.fromBytes(bytes, classType);
    }

    @Override
    public void delete(String key) {
        objectStoreDao.delete(key);
    }

}
