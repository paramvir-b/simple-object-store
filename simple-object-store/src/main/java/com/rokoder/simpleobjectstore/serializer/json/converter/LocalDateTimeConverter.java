package com.rokoder.simpleobjectstore.serializer.json.converter;

import com.google.gson.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class LocalDateTimeConverter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ISODateTimeFormat.dateHourMinuteSecondMillis().print(localDateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ISODateTimeFormat.dateHourMinuteSecondMillis().parseLocalDateTime(
                jsonElement.getAsJsonPrimitive().getAsString());
    }
}
