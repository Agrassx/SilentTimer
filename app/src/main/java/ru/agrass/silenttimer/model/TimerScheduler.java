package ru.agrass.silenttimer.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Calendar;
import java.util.Map;

import ru.agrass.silenttimer.model.entity.Timer;
import ru.agrass.silenttimer.model.entity.Week;
import ru.agrass.silenttimer.model.exceptions.DayNotFoundException;
import ru.agrass.silenttimer.model.exceptions.WeekException;
import ru.agrass.silenttimer.model.parsers.TimeParser;
import ru.agrass.silenttimer.utils.AlarmManagerUtil;
import ru.agrass.silenttimer.utils.CalendarUtil;
import ru.agrass.silenttimer.utils.IntentTimerUtil;
import ru.agrass.silenttimer.utils.SoundSwitchUtil;

public class TimerScheduler {
    private static final String TAG = TimerScheduler.class.getSimpleName();

    public void setUpTimer(Timer timer) {
        try {
            boolean isDayMode = isDayMode(timer);
            if (isOnlyOnce(timer)) {
                startOnce(timer, isDayMode);
                return;
            }
            if (Week.isInCurrentDay(timer.getDaysMap(), CalendarUtil.getCurrentDayOfWeek())) {
                startToDay(timer, isDayMode);
                return;
            }
            startAtNewDay(timer);
        } catch (DayNotFoundException e) {
            Log.e(TAG, e.getMessage());
            Crashlytics.log(e.getMessage());
        }
    }

    private void startToDay(Timer timer, boolean isDayMode) {
        try {
            if (isCurrentTimeLessThenTimerFrom(timer)) {
                Log.i(TAG, "Timer start today" + timer.toString());
                startAtTimeFrom(timer);
            } else if (isTimeBetweenFromAndTo(timer, isDayMode)) {
                Log.i(TAG, "Timer start now" + timer.toString());
                startNow();
                startAtTimeTo(timer, isDayMode);
            } else if (isCurrentMoreThenTimeTo(timer, isDayMode)) {
                startAtNewDay(timer);
            } else {
                Crashlytics.log("Bad timer: " + timer.toString());
                throw new Exception("Bad timer: " + timer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startOnce(Timer timer, boolean isDayMode) {
        Log.i(TAG, "Timer is only once " + timer.toString());
        try {
            if (isCurrentTimeLessThenTimerFrom(timer)) {
                startAtTimeFrom(timer);
            } else if (isTimeBetweenFromAndTo(timer, isDayMode)) {
                startNow();
                startAtTimeTo(timer, isDayMode);
            } else if (isCurrentMoreThenTimeTo(timer, isDayMode)) {
                stopTimer(timer);
            } else {
                throw new Exception("Bad timer: " + timer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    private void stopTimer(Timer timer) {
        AlarmManagerUtil.stopAlarm(IntentTimerUtil.getStopIntent(timer));
    }

    private void startOnThisWeek(Timer timer, String nearDay) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom()),
                nearDay
        );

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                from.getTimeInMillis()
        );
    }

    private void startOnNextWeek(Timer timer, String nearDay) {
        Calendar from = CalendarUtil.getCalendarOfNextWeek(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom()),
                nearDay
        );

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                from.getTimeInMillis()
        );
    }

    private boolean isOnlyOnce(Timer timer) {
        for (Map.Entry<String, Boolean> entry : timer.getDaysMap().entrySet()) {
            if (entry.getValue()) return false;
        }
        return true;
    }

    public void updateTimer(Timer timer) {
        if (timer.isEnable()) {
//            stopTimer(timer);
            setUpTimer(timer);
        } else {
            cancelTimer(timer);
        }
    }

    public void cancelTimer(Timer timer) {
        AlarmManagerUtil.cancelAlarm(
                IntentTimerUtil.getStopIntent(timer)
        );
        SoundSwitchUtil.soundTurnOn();
    }

    private void startNow() {
        SoundSwitchUtil.soundTurnOff();
    }

    private boolean isDayMode(Timer timer) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );
        Calendar to = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        return from.getTimeInMillis() < to.getTimeInMillis();
    }

    private void startAtTimeTo(Timer timer, boolean isDayMode) {

        Calendar calendar = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );

        if (!isDayMode) {
            calendar = CalendarUtil.addDay(calendar);
        }

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, false),
                calendar.getTimeInMillis()
        );
    }

    private void startAtNewDay(Timer timer) {
        try {
            String nearDay = Week.findNearestDay(
                    timer.getDaysMap(),
                    CalendarUtil.getCurrentDayOfWeek()
            );

            Log.i(TAG, "Timer start at " + nearDay + " " + timer.toString());

            if (Week.isDayOnThisWeek(CalendarUtil.getCurrentDayOfWeek(), nearDay)) {
                startOnThisWeek(timer, nearDay);
                return;
            }
            startOnNextWeek(timer, nearDay);
        } catch (WeekException e) {
            Crashlytics.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startAtTimeFrom(Timer timer) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                from.getTimeInMillis()
        );
    }

    private boolean isCurrentTimeLessThenTimerFrom(@NonNull Timer timer) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );
        return CalendarUtil.isCurrentLessThenTimeFrom(from);
    }

    private boolean isTimeBetweenFromAndTo(@NonNull Timer timer, boolean isDayMode) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );
        Calendar to = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        if (!isDayMode) {
            to = CalendarUtil.addDay(to);
        }
        return CalendarUtil.isCurrentTimeBetweenFromAndTo(from, to);
    }

    private boolean isCurrentMoreThenTimeTo(@NonNull Timer timer, boolean isDayMode) {
        Calendar to = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        long timeTo = to.getTimeInMillis();
        long current = CalendarUtil.getCurrentTimeCalendar().getTimeInMillis();

        Log.e(TAG, "timeTo = " + timeTo);
        Log.e(TAG, "current = " + current);

        return CalendarUtil.isCurrentMoreThenTimeTo(to);
    }
}
