package com.rokoder.simpleobjectstore.serializer.xml;


import com.rokoder.simpleobjectstore.serializer.ObjectSerializer;
import com.rokoder.simpleobjectstore.serializer.xml.converter.DateTimeConverter;
import com.rokoder.simpleobjectstore.serializer.xml.converter.LocalDateConverter;
import com.rokoder.simpleobjectstore.serializer.xml.converter.LocalDateTimeConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

public class ObjectXmlSerializer implements ObjectSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectXmlSerializer.class);
    private final XStream xstream;
    private final boolean isCompact;

    public ObjectXmlSerializer() {
        this.xstream = createDefaultXStream();
        this.isCompact = true; // By default we use compact writer
    }

    public ObjectXmlSerializer(boolean isCompact) {
        this.xstream = createDefaultXStream();
        this.isCompact = isCompact;
    }

    public ObjectXmlSerializer(XStream xstream, boolean isCompact) {
        this.xstream = xstream;
        this.isCompact = isCompact;
    }

    private XStream createDefaultXStream() {
        XStream xs = new XStream();
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
