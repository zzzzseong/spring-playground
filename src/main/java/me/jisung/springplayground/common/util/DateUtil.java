package me.jisung.springplayground.common.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_ZONE = "Asia/Seoul";

    private DateUtil() {}

    public static String now() {
        return now(DATE_TIME_FORMAT);
    }
    public static String now(String format) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
        return now.format(DateTimeFormatter.ofPattern(format));
    }
}
