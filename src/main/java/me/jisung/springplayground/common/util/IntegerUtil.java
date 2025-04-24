package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "IntegerUtil")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerUtil {

    private static final int DEFAULT_VALUE = 1;

    public static int parseInt(String number) {
        return parseInt(number, DEFAULT_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int parseInt(String number, int defaultValue) {
        return parseInt(number, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int parseInt(String number, int defaultValue, int minValue, int maxValue) {
        // validate null or empty string
        if(number == null || number.isEmpty()) return defaultValue;

        try {
            int i = Integer.parseInt(number);
            return Math.max(Math.min(i, maxValue), minValue);
        } catch (NumberFormatException e) {
            // Return default value if not a number or out of integer range
            return defaultValue;
        }
    }

}
