package com.rokoder.simpleobjectstore.serializer.xml.converter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeConverter extends AbstractSingleValueConverter {
    @Override
    public boolean canConvert(Class type) {
        return type == DateTime.class;
    }

    @Override
    public Object fromString(String str) {
        return ISODateTimeFormat.dateTime().parseDateTime(str);
    }

    @Override
    public String toString(Object obj) {
        return ISODateTimeFormat.dateTime().print((DateTime) obj);
    }
}
