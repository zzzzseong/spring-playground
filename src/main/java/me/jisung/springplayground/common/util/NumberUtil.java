package me.jisung.springplayground.common.util;

import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NumberUtil {

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    private static final Random random = new Random();

    public static String format(int number) {
        return numberFormat.format(number);
    }

    public static int generateRandomNumber(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
