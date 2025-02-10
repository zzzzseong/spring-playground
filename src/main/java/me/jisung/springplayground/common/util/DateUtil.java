package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static final String DATE_TIME_FORMAT         = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_SHORT   = "yyyy-MM-dd";

    public static final String DATE_TIME_ZONE = "Asia/Seoul";

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
    }
    public static String now(String format) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(ZonedDateTime datetime) {
        return datetime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
    public static String format(ZonedDateTime datetime, String format) {
        return datetime.format(DateTimeFormatter.ofPattern(format));
    }

    public static ZonedDateTime startOfDay(ZonedDateTime datetime) {
        return datetime.withHour(0).withMinute(0).withSecond(0);
    }
    public static ZonedDateTime endOfDay(ZonedDateTime datetime) {
        return datetime.withHour(23).withMinute(59).withSecond(59);
    }
    public static int lastDayOfMonth(ZonedDateTime datetime) {
        return datetime.toLocalDate().lengthOfMonth();
    }
}
