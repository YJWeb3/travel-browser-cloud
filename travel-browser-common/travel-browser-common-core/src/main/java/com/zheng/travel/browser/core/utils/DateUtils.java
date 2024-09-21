package com.zheng.travel.browser.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DateUtils {

    public static long getLastMillisSeconds() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastSeconds = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        return Duration.between(now, lastSeconds).toMillis();
    }

    public static long getLastMillisSecondsOld() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 29);
        long lastMillis = instance.getTimeInMillis();
        return lastMillis - System.currentTimeMillis();
    }
}
