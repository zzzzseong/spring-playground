package me.jisung.springplayground.common.util;

import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NumberUtil {

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    public static String format(int number) {
        return numberFormat.format(number);
    }
}
