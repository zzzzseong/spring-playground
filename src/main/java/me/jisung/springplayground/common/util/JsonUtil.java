package me.jisung.springplayground.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {

    private static final Gson gson = new Gson();
    private static final Gson nonEscapeGson = new GsonBuilder().disableHtmlEscaping().create();


    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    public static String toNonEscapeJson(Object object) {
        return nonEscapeGson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
