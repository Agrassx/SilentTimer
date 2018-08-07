package ru.agrass.silenttimer.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import ru.agrass.silenttimer.model.Week;


public class CalendarUtil {

    private static final String TAG = CalendarUtil.class.getSimpleName();

    public static Calendar getCalendar(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static Calendar getCalendar(int hour, int minute, int addDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, addDays);
        return calendar;
    }

    public static Calendar getCalendar(int hour, int minute, String day) {
        int dayCount = Week.getIntFromString(day)
                - Week.getIntFromString(getCurrentDayOfWeek());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, dayCount);
        return calendar;
    }

    public static Calendar getCalendarOfNextWeek(int hour, int minute, String day) {
        int dayCount = Week.getIntFromString(Week.SUNDAY)
                - Week.getIntFromString(getCurrentDayOfWeek()) + Week.getIntFromString(day) + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, dayCount);
        return calendar;
    }

    public static Calendar getCurrentTimeCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }

    public static String getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        String day = calendar.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                Locale.ENGLISH
        );

        Log.e(TAG, "Current day: " + day);
        switch (day) {
            case Week.LONG_MONDAY:
                return Week.MONDAY;
            case Week.LONG_TUESDAY:
                return Week.TUESDAY;
            case Week.LONG_WEDNESDAY:
                return Week.WEDNESDAY;
            case Week.LONG_THURSDAY:
                return Week.THURSDAY;
            case Week.LONG_FRIDAY:
                return Week.FRIDAY;
            case Week.LONG_SATURDAY:
                return Week.SATURDAY;
            case Week.LONG_SUNDAY:
                return Week.SUNDAY;
            default:
                return "";
        }
    }

    public static Calendar addDay(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        return calendar;
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
