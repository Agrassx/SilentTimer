package ru.agrass.silenttimer.model.parsers;

public class TimeParser {

    private static final String SPLITTER = ":";
    private static final String ZERO = "0";
    private static final int TYPE_HOUR = 0;
    private static final int TYPE_MINUTE = 1;


    //    First always hour, second - minutes
    @Deprecated
    public static int[] stringToInt(String time) {
        String[] times = time.split(SPLITTER);
        int[] intTime = new int[2];

        for (int i = 0; i < times.length - 1; i++) {
            if (times[i].startsWith(ZERO)) {
                intTime[i] = Integer.valueOf(times[i].substring(1));
            }
        }
        return intTime;
    }

    public static int getIntMinuteFromString(String time) {
        return getTime(time, TYPE_MINUTE);
    }

    public static int getIntHourFromString(String time) {
        return getTime(time, TYPE_HOUR);
    }

    private static int getTime(String time, int timeType) {
        String[] times = time.split(SPLITTER);
        int intTime;

        if (times[timeType].startsWith(ZERO)) {
            intTime = Integer.valueOf(times[timeType].substring(1));
        } else {
            intTime = Integer.valueOf(times[timeType]);
        }

        return intTime;
    }

    public static String intToString(int hour, int minute) {
        String minuteStr = String.valueOf(minute);
        String hourStr = String.valueOf(hour);
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        if (hour < 10) {
            hourStr = "0" + hour;
        }
        return hourStr + SPLITTER + minuteStr;
    }
}
