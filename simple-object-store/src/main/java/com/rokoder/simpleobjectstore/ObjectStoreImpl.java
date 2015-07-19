package com.rokoder.simpleobjectstore;

import com.rokoder.simpleobjectstore.dao.ObjectStoreDao;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService scheduledES;

    public ObjectStoreImpl(String name, ObjectStoreDao objectStoreDao, ObjectSerializer objectSerializer) {
        this(name, objectStoreDao, objectSerializer, false, 0);
    }

    public ObjectStoreImpl(String name, ObjectStoreDao objectStoreDao, ObjectSerializer objectSerializer, boolean createSchema, int deleteExpFreqSeconds) {
        this.name = name;
        this.objectStoreDao = objectStoreDao;
        this.objectSerializer = objectSerializer;

        if (createSchema) {
            objectStoreDao.createTableSchema();
        }

        setupDeletionThread(deleteExpFreqSeconds);
    }


    private void setupDeletionThread(int deleteFreqSeconds) {
        if (deleteFreqSeconds < 0) {
            throw new IllegalArgumentException(
                    "deleteFreqSeconds cannot be less than zero deleteFreqSeconds=" + deleteFreqSeconds);
        }

        if (deleteFreqSeconds == 0) {
            LOGGER.info("No automatic deletion thread setup as deleteFreqSeconds={}", deleteFreqSeconds);
            return;
        }

        LOGGER.info("Creating automatic deletion thread with deleteFreqSeconds={}", deleteFreqSeconds);
        scheduledES = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                // We can make thread as daemon as its ok to have some expired objects left behind. We can delete them
                // next time. This way we do not have to
                thread.setDaemon(true);
                thread.setName("obj-str-del");
                return thread;
            }
        });

        scheduledES.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                LocalDateTime now = new LocalDateTime();
                LOGGER.trace("Deleting expired objects for time={}", now);
                objectStoreDao.deleteByExpireTime(now);
            }
        }, 0, deleteFreqSeconds, TimeUnit.SECONDS);
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
