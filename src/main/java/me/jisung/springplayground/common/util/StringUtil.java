package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    private static final String NULL = "null";


    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || NULL.equals(value);
    }

    public static String getOrDefault(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    public static String ifEmpty(String target, String defaultValue, String nonNullValue) {
        return isEmpty(target) ? defaultValue : nonNullValue;
    }

    public static String build(String...parts) {
        StringBuilder builder = new StringBuilder();
        for (String part : parts) builder.append(part);
        return builder.toString();
    }
}
