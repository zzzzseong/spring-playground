package me.jisung.springplayground.common.util;

import java.util.Objects;

public class StringUtil {

    private static final String NULL = "null";

    private StringUtil() {}

    public static boolean isEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty() || Objects.equals(NULL, value);
    }

    public static String getOrDefault(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    public static String ifNull(String target, String defaultValue, String nonNullValue) {
        return Objects.isNull(target) ? defaultValue : nonNullValue;
    }
}
