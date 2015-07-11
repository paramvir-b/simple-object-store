package com.rokoder.simpleobjectstore.serializer.xml.converter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class LocalDateTimeConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class type) {
        return type == LocalDateTime.class;
    }

    @Override
    public Object fromString(String str) {
        return ISODateTimeFormat.dateHourMinuteSecondMillis().parseLocalDateTime(str);
    }

    @Override
    public String toString(Object obj) {
        return ISODateTimeFormat.dateHourMinuteSecondMillis().print((LocalDateTime) obj);
    }
}
