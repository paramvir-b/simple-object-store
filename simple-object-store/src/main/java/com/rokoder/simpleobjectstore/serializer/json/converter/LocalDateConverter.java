package com.rokoder.simpleobjectstore.serializer.json.converter;

import com.google.gson.*;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ISODateTimeFormat.date().print(localDate));
    }

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ISODateTimeFormat.date().parseLocalDate(jsonElement.getAsJsonPrimitive().getAsString());
    }

}

