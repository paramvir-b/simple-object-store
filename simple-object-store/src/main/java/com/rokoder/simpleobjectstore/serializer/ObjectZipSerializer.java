package com.rokoder.simpleobjectstore.serializer;

import com.rokoder.simpleobjectstore.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ObjectZipSerializer implements ObjectSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectZipSerializer.class);

    private final ObjectSerializer wrapObj;

    public ObjectZipSerializer(ObjectSerializer wrapObj) {
        this.wrapObj = wrapObj;
    }

    private void writeZippedBytesToByteStream(ByteArrayOutputStream bos, byte[] unzipBytes) {
        try {
            GZIPOutputStream gzos = new GZIPOutputStream(bos);
            gzos.write(unzipBytes);
            gzos.flush();
            gzos.close();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to convert to \nbytes(" + unzipBytes.length + ")=" + StringUtil.byteArrayToHexString(
                            unzipBytes), e);
        }
    }

    @Override
    public byte[] toBytes(Object obj) {
        byte[] unzipBytes = wrapObj.toBytes(obj);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeZippedBytesToByteStream(bos, unzipBytes);

        byte[] zipBytes = bos.toByteArray();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={} \nunzipBytes({})={}\n" + "zipBytes({})={}", obj.getClass(), obj,
                    unzipBytes.length, StringUtil.byteArrayToHexString(unzipBytes), zipBytes.length,
                    StringUtil.byteArrayToHexString(zipBytes));
        }

        return zipBytes;
    }

    private <T> byte[] zipBytesToUnzipBytes(ByteArrayInputStream bis, Class<T> classType) {
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(bis);
            ByteArrayOutputStream unzipBos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int bytesRead = 0;

            do {
                bytesRead = gzipInputStream.read(bytes);
                if (bytesRead > 0) {
                    unzipBos.write(bytes, 0, bytesRead);
                }
            } while (bytesRead > 0);

            bis.close();
            return unzipBos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load bytes becasue of I/O Exception to object type=" + classType,
                    e);
        }
    }

    @Override
    public <T> T fromBytes(byte[] bytes, Class<T> classType) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        byte[] unzipBytes = zipBytesToUnzipBytes(bis, classType);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} \nzipBytes({})={}\n" + "unzipBytes({})={}", classType, bytes.length,
                    StringUtil.byteArrayToHexString(bytes), unzipBytes.length,
                    StringUtil.byteArrayToHexString(unzipBytes));
        }

        return wrapObj.fromBytes(unzipBytes, classType);
    }
}
