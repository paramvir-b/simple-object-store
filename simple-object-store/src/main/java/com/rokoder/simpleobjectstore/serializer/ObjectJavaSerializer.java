package com.rokoder.simpleobjectstore.serializer;

import com.rokoder.simpleobjectstore.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ObjectJavaSerializer implements ObjectSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectJavaSerializer.class);

    private void writeObjToByteStream(ByteArrayOutputStream bos, Object object) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to convert to bytes object=" + object, e);
        }
    }

    @Override
    public byte[] toBytes(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeObjToByteStream(bos, obj);
        byte[] bytes = bos.toByteArray();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={} \nbytes({})={}", obj.getClass(), obj, bytes.length,
                    StringUtil.byteArrayToHexString(bytes));
        }
        return bytes;
    }

    private <T> T fromBytes(ByteArrayInputStream bis, Class<T> classType) {
        try {
            T retObj = (T) (new ObjectInputStream(bis)).readObject();
            bis.close();
            return retObj;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load bytes becasue of I/O Exception to object type=" + classType,
                    e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load bytes as object type=" + classType + " not found", e);
        }
    }

    @Override
    public <T> T fromBytes(byte[] bytes, Class<T> classType) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        T obj = fromBytes(bis, classType);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={}", obj.getClass(), obj);
        }
        return obj;
    }
}
