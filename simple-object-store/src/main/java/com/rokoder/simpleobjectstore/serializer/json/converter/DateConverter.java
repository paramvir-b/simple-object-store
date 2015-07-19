package com.rokoder.simpleobjectstore.serializer.json.converter;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Convert for GSON lib to retain milli seconds accuracy
 */
public class DateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(ISODateTimeFormat.dateTime().print(new DateTime(date)));
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DateTime dateTime = ISODateTimeFormat.dateTime().parseDateTime(jsonElement.getAsJsonPrimitive().getAsString());
        return dateTime.toDate();
    }

}
