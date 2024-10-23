package me.jisung.springplayground.common.util;

import java.util.Objects;

public class StringUtil {

    private StringUtil() { throw new IllegalStateException("util class cannot be instantiated"); }

    public static boolean isEmptyString(String str) {
        return Objects.isNull(str) || str.isEmpty() || Objects.equals("null", str);
    }
}
