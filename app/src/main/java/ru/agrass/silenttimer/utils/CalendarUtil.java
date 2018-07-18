package ru.agrass.silenttimer.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;


public class CalendarUtil {

    public static Calendar getCalendar(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    public static Calendar getCurrentTimeCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }

    public static Calendar addDay(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        return calendar;
    }

    public static boolean isLess(Calendar calendar, Calendar another) {
        return calendar.getTimeInMillis() < another.getTimeInMillis();
    }

    public static boolean isCurrentLessThenTimeFrom(@NonNull Calendar time) {
        return getCurrentTimeCalendar().getTimeInMillis() < time.getTimeInMillis();
    }

    public static boolean isCurrentTimeBetweenFromAndTo(@NonNull Calendar from, @NonNull Calendar to) {
        Calendar current = getCurrentTimeCalendar();
        return current.getTimeInMillis() > from.getTimeInMillis() &&
                current.getTimeInMillis() < to.getTimeInMillis();
    }

    public static boolean isCurrentMoreThenTimeTo(@NonNull Calendar time) {
        return getCurrentTimeCalendar().getTimeInMillis() > time.getTimeInMillis();
    }


}
