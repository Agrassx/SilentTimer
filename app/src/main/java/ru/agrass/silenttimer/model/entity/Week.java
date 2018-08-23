package ru.agrass.silenttimer.model.entity;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import ru.agrass.silenttimer.model.exceptions.DayNotFoundException;
import ru.agrass.silenttimer.model.exceptions.IncorrectDayException;
import ru.agrass.silenttimer.model.exceptions.WeekException;

public class Week {
//    TODO: Replace map on week
//    TODO: Check input strings (IncorrectDayException)

    public static final int COUNT_OF_DAYS = 7;

    public static final String MONDAY = "Mo";
    public static final String TUESDAY = "Tu";
    public static final String WEDNESDAY = "We";
    public static final String THURSDAY = "Th";
    public static final String FRIDAY = "Fr";
    public static final String SATURDAY = "Sa";
    public static final String SUNDAY = "Su";


    public static final String LONG_MONDAY = "Monday";
    public static final String LONG_TUESDAY = "Tuesday";
    public static final String LONG_WEDNESDAY = "Wednesday";
    public static final String LONG_THURSDAY = "Thursday";
    public static final String LONG_FRIDAY = "Friday";
    public static final String LONG_SATURDAY = "Saturday";
    public static final String LONG_SUNDAY = "Sunday";


    public static final int INT_MONDAY = 0;
    public static final int INT_TUESDAY = 1;
    public static final int INT_WEDNESDAY = 2;
    public static final int INT_THURSDAY = 3;
    public static final int INT_FRIDAY = 4;
    public static final int INT_SATURDAY = 5;
    public static final int INT_SUNDAY = 6;

    private Map<String, Boolean> daysMap;

    public Week(Map<String, Boolean> daysMap) {
        this.daysMap = daysMap;
    }

    public Map<String, Boolean> getDaysMap() {
        return daysMap;
    }

    @Deprecated
    public static Map<String, Boolean> initWeek() {
        Map<String, Boolean> daysMap = new ArrayMap<>(COUNT_OF_DAYS);
        daysMap.put(Week.MONDAY, false);
        daysMap.put(Week.TUESDAY, false);
        daysMap.put(Week.WEDNESDAY, false);
        daysMap.put(Week.THURSDAY, false);
        daysMap.put(Week.FRIDAY, false);
        daysMap.put(Week.SATURDAY, false);
        daysMap.put(Week.SUNDAY, false);
        return daysMap;
    }

    public static Week initWeek_() {
        return null;
    }

    public static int getIntFromString(String longOrShortWeekDay) {
        switch (longOrShortWeekDay) {
            case MONDAY:            return INT_MONDAY;
            case TUESDAY:           return INT_TUESDAY;
            case WEDNESDAY:         return INT_WEDNESDAY;
            case THURSDAY:          return INT_THURSDAY;
            case FRIDAY:            return INT_FRIDAY;
            case SATURDAY:          return INT_SATURDAY;
            case SUNDAY:            return INT_SUNDAY;

            case LONG_MONDAY:       return INT_MONDAY;
            case LONG_TUESDAY:      return INT_TUESDAY;
            case LONG_WEDNESDAY:    return INT_WEDNESDAY;
            case LONG_THURSDAY:     return INT_THURSDAY;
            case LONG_FRIDAY:       return INT_FRIDAY;
            case LONG_SATURDAY:     return INT_SATURDAY;
            case LONG_SUNDAY:       return INT_SUNDAY;

            default: throw new IncorrectDayException("Incorrect day format: " + longOrShortWeekDay);
        }
    }

    public static String getShortString(int day) throws IndexOutOfBoundsException {

        if (day < 0 || day > 6) {
            throw new IndexOutOfBoundsException(
                    "Day must be in [0-6] interval, where 0 is Monday, 6 is Sunday"
            );
        }

        switch (day) {
            case INT_MONDAY:       return MONDAY;
            case INT_TUESDAY:      return TUESDAY;
            case INT_WEDNESDAY:    return WEDNESDAY;
            case INT_THURSDAY:     return THURSDAY;
            case INT_FRIDAY:       return FRIDAY;
            case INT_SATURDAY:     return SATURDAY;
            case INT_SUNDAY:       return SUNDAY;
        }
        return "";
    }

    public static String getShortString(String longDay) {
        switch (longDay) {
            case LONG_MONDAY:       return MONDAY;
            case LONG_TUESDAY:      return TUESDAY;
            case LONG_WEDNESDAY:    return WEDNESDAY;
            case LONG_THURSDAY:     return THURSDAY;
            case LONG_FRIDAY:       return FRIDAY;
            case LONG_SATURDAY:     return SATURDAY;
            case LONG_SUNDAY:       return SUNDAY;
        }
        throw new IncorrectDayException("Incorrect day name: " + longDay);
    }

    public static boolean isDayOnThisWeek(@NonNull String currentDay, @NonNull String nearDay) {
        return getIntFromString(currentDay) < getIntFromString(nearDay);
    }

    public static String findNearestDay(@NonNull Map<String, Boolean> weekMap, @NonNull String currentDayOfWeek) throws WeekException {
        int from = Week.getIntFromString(currentDayOfWeek);

//        On this week
        for (int i = from + 1; i < COUNT_OF_DAYS; i++) {
            if (weekMap.get(Week.getShortString(i))) {
                return getShortString(i);
            }
        }

//        On next week
        for (int i = 0; i < COUNT_OF_DAYS - from; i++) {
            if (weekMap.get(Week.getShortString(i))) {
                return getShortString(i);
            }
        }

        throw new WeekException("Nearest day not found: current day: " + currentDayOfWeek
                + " week: " + weekMap.toString());
    }

    public static boolean isInCurrentDay(@NonNull Map<String, Boolean> dayMap, @NonNull String currentDay) throws DayNotFoundException {
        if (!dayMap.containsKey(currentDay)) {
            throw new DayNotFoundException("daysMap not contain day: " + currentDay);
        }
        return dayMap.get(currentDay);
    }

}
