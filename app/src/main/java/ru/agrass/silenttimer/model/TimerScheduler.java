package ru.agrass.silenttimer.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;
import ru.agrass.silenttimer.model.parsers.TimeParser;
import ru.agrass.silenttimer.utils.AlarmManagerUtil;
import ru.agrass.silenttimer.utils.CalendarUtil;
import ru.agrass.silenttimer.utils.IntentTimerUtil;
import ru.agrass.silenttimer.utils.SoundSwitchUtil;

public class TimerScheduler {
    private static final String TAG = TimerScheduler.class.getSimpleName();

    public void startTimer(Timer timer) {
        Log.e(TAG, "Start Timer");
        try {
            boolean isDayMode = isDayMode(timer);

            if (isCurrentTimeLessThenTimerFrom(timer)) {
                startAtTimeFrom(timer);
                Log.e(TAG, "Current Time Less Then Timer From");
            } else if (isTimeBetweenFromAndTo(timer, isDayMode)) {
                startNow();
                startAtTimeTo(timer, isDayMode);
                Log.e(TAG, "Time Between From And To");
            } else if (isCurrentMoreThenTimeTo(timer, isDayMode)) {
                startAtNewDay(timer, isDayMode);
                Log.e(TAG, "Current Time More Then TimeTo");
            } else {
                Crashlytics.log("Bad timer: " + timer.toString());
                throw new Exception("Bad timer: " + timer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTimer(Timer timer) {
        cancelTimer(timer);
        startTimer(timer);
    }

    public void cancelTimer(Timer timer) {
        cancelNow(timer);
    }

    private void cancelNow(Timer timer) {
        AlarmManagerUtil.cancelAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, false)
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

        if(!isDayMode) {
            calendar = CalendarUtil.addDay(calendar);
        }

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, false),
                calendar.getTimeInMillis()
        );
    }

    private void startAtNewDay(Timer timer, boolean isDayMode) {
        Calendar newDay = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );

        if (isDayMode) {
            newDay = CalendarUtil.addDay(newDay);
        }

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                newDay.getTimeInMillis()
        );
        Log.i(TAG, "New Timer (Sound Off) start at " + newDay.getTimeInMillis());
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
        return CalendarUtil.isCurrentMoreThenTimeTo(to);
    }
}
