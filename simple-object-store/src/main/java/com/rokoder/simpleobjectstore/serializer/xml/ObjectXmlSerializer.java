package com.rokoder.simpleobjectstore.serializer.xml;


import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import com.rokoder.simpleobjectstore.serializer.xml.converter.DateTimeConverter;
import com.rokoder.simpleobjectstore.serializer.xml.converter.LocalDateConverter;
import com.rokoder.simpleobjectstore.serializer.xml.converter.LocalDateTimeConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

/**
 * XML serializer to convert objects to byte array
 */
public class ObjectXmlSerializer implements ObjectSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectXmlSerializer.class);
    private final XStream xstream;
    private final boolean isCompact;

    /**
     * Default XML serializer created with default converters for joda-time date classes.
     */
    public ObjectXmlSerializer() {
        this.xstream = createDefaultXStream();
        this.isCompact = true; // By default we use compact writer
    }

    /**
     * Default XML serializer created with default converters joda-time date classes. In addition you can control
     * output format.
     *
     * @param isCompact If true then object will be serialized to compact XML format
     */
    public ObjectXmlSerializer(boolean isCompact) {
        this.xstream = createDefaultXStream();
        this.isCompact = isCompact;
    }

    /**
     * If you want to provide your own @{link XStream} object. You have to customize all by yourself. This way it won't register
     * default converters or apply any changes to passed @{link XStream} object. You have full control.
     *
     * @param xstream   User specified @{link XStream} object which will be used for serialization
     * @param isCompact If true then object will be serialized to compact XML format
     */
    public ObjectXmlSerializer(XStream xstream, boolean isCompact) {
        this.xstream = xstream;
        this.isCompact = isCompact;
    }

    private XStream createDefaultXStream() {
        XStream xs = new XStream();
        xs.addPermission(AnyTypePermission.ANY);
        xs.registerConverter(new LocalDateConverter());
        xs.registerConverter(new LocalDateTimeConverter());
        xs.registerConverter(new DateTimeConverter());
        return xs;
    }

    @Override
    public byte[] toBytes(Object obj) {
        String xmlStr;

        if (isCompact) {
            StringWriter sw = new StringWriter();
            xstream.marshal(obj, new CompactWriter(sw));
            xmlStr = sw.toString();
        } else {
            xmlStr = xstream.toXML(obj);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={} xmlStr=\n{}", obj.getClass(), obj, xmlStr);
        }

        return xmlStr.getBytes();
    }

    @Override
    public <T> T fromBytes(byte[] bytes, Class<T> classType) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes);

        T obj = (T) xstream.fromXML(str);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("objclass={} obj={}", obj.getClass(), obj);
        }

        return obj;
    }
}
