package com.rokoder.simpleobjectstore.serializer.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import com.rokoder.simpleobjectstore.serializer.json.converter.DateConverter;
import com.rokoder.simpleobjectstore.serializer.json.converter.DateTimeConverter;
import com.rokoder.simpleobjectstore.serializer.json.converter.LocalDateConverter;
import com.rokoder.simpleobjectstore.serializer.json.converter.LocalDateTimeConverter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Json serializer to convert objects to byte array
 */
public class ObjectJsonSerializer implements ObjectSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectJsonSerializer.class);
    private final Gson gson;

    /**
     * Default json serializer created with default converters for java Date and joda-time date classes.
     */
    public ObjectJsonSerializer() {
        gson = createGson(true);
    }

    /**
     * Default json serializer created with default converters for java Date and joda-time date classes. In addition
     * you can control output format.
     *
     * @param isCompact If true then object will be serialized to compact json format
     */
    public ObjectJsonSerializer(boolean isCompact) {
        gson = createGson(isCompact);
    }

    /**
     * If you want to provide your own @{link Gson} object. You have to customize all by yourself. This way it won't register
     * default converters or apply any changes to passed @{link Gson} object. You have full control.
     *
     * @param gson User specified @{link Gson} object which will be used for serialization
     */
    public ObjectJsonSerializer(Gson gson) {
        this.gson = gson;
    }

    private Gson createGson(boolean isCompact) {
        Gson retGson = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateConverter());
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateConverter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter());
        if (isCompact) {
            retGson = gsonBuilder.create();
        } else {
            retGson = gsonBuilder.setPrettyPrinting().create();
        }

        return retGson;
    }

    @Override
    public byte[] toBytes(Object obj) {
        String jsonStr = gson.toJson(obj);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={} jsonStr=\n{}", obj.getClass(), obj, jsonStr);
        }

        return jsonStr.getBytes();
    }

    @Override
    public <T> T fromBytes(byte[] bytes, Class<T> classType) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes);

        T obj = gson.fromJson(str, classType);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={}", obj.getClass(), obj);
        }

        return obj;

    }
}
