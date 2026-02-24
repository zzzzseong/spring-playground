package me.jisung.springplayground.common.util;

import com.google.gson.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    private static final Gson nonEscapeGson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .disableHtmlEscaping()
            .create();


    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    public static String toNonEscapeJson(Object object) {
        return nonEscapeGson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }


    /**
     * Adapter for ZonedDateTime Serialize/Deserialize
     * */
    private static class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_INSTANT));
        }

        @Override
        public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                final String dateTimeString = jsonElement.getAsString();
                return ZonedDateTime.parse(dateTimeString);
            } catch (Exception e) {
                throw new JsonParseException("Failed to new instance", e);
            }
        }
    }
}
