package com.rokoder.simpleobjectstore.serializer.xml.converter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

public class LocalDateConverter extends AbstractSingleValueConverter {


    @Override
    public boolean canConvert(Class type) {
        return type == LocalDate.class;
    }

    @Override
    public Object fromString(String str) {
        return ISODateTimeFormat.date().parseLocalDate(str);
    }

    @Override
    public String toString(Object obj) {
        return ISODateTimeFormat.date().print((LocalDate) obj);
    }
}
