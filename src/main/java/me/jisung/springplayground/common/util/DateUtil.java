package me.jisung.springplayground.common.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

}
