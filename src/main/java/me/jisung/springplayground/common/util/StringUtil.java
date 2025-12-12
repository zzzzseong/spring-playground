package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    private static final String NULL = "null";


    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || NULL.equals(value);
    }
    public static boolean isEmpty(Character value) {
        return value == null || value == '\u0000';
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

    public static boolean equalsAny(String target, String... values) {
        if (isEmpty(target) || values == null) return false;

        for (String value : values) {
            if (Objects.equals(target, value)) return true;
        }

        return false;
    }

    public static String generateRandomString(int length) {
        byte[] bytes = new byte[length];

        new SecureRandom().nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes)
                .substring(0, length);
    }
}
