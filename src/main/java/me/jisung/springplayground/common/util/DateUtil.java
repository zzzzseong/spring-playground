package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static final String DATE_FORMAT      = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_ZONE = "Asia/Seoul";

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
    }
    public static String now(String format) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(DateUtil.DATE_TIME_ZONE));
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(ZonedDateTime datetime) {
        return format(datetime, DATE_TIME_FORMAT);
    }
    public static String format(ZonedDateTime datetime, String format) {
        if(datetime == null) throw new IllegalArgumentException("datetime must not be null");

        try {
            return datetime.format(DateTimeFormatter.ofPattern(format));
        } catch (IllegalArgumentException e) {
            log.error("Exception occurred while formatting datetime: {}", e.getMessage(), e);
            return datetime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        }
    }

    /**
     * 하루의 시작 시각을 반환합니다. (시: 00, 분: 00, 초: 00)
     * @param datetime 기준이 되는 날짜 및 시간
     * @return 하루의 시작 시각을 나타내는 {@link ZonedDateTime}
     * */
    public static ZonedDateTime startOfDay(ZonedDateTime datetime) {
        return datetime.withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * 하루의 종료 시각을 반환합니다. (시: 23, 분: 59, 초: 59)
     * @param datetime 기준이 되는 날짜 및 시간
     * @return 하루의 종료 시각을 나타내는 {@link ZonedDateTime}
     * */
    public static ZonedDateTime endOfDay(ZonedDateTime datetime) {
        return datetime.withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 해당 월의 마지막 날짜를 반환합니다.
     * @param datetime 기준이 되는 날짜 및 시간
     * @return 해당 월의 마지막 날짜 (예: 28, 30, 31 등)
     * */
    public static int lastDayOfMonth(ZonedDateTime datetime) {
        return datetime.toLocalDate().lengthOfMonth();
    }
}
