package me.jisung.springplayground.common.util;

import com.google.gson.Gson;

public class JsonUtil {

    private JsonUtil() { throw new IllegalStateException("Utility class cannot be instantiated"); }

    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
