package ru.agrass.silenttimer.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;

import ru.agrass.silenttimer.model.parsers.TimeParser;
import ru.agrass.silenttimer.utils.AlarmManagerUtil;
import ru.agrass.silenttimer.utils.CalendarUtil;
import ru.agrass.silenttimer.utils.IntentTimerUtil;
import ru.agrass.silenttimer.utils.SoundSwitchUtil;

public class TimerScheduler {
    private static final String TAG = TimerScheduler.class.getSimpleName();

    public void startTimer(Timer timer) {
        try {
            startWithDayMode(timer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startWithDayMode(Timer timer) throws Exception {
        if (isCurrentTimeLessThenTimerFrom(timer)) {
            startAtTimeFrom(timer);
            return;
        }
        if (isTimeBetweenFromAndTo(timer)) {
            startNow(timer);
            startAtTimeTo(timer);
            return;
        }
        if (isCurrentMoreThenTimeTo(timer)) {
            startAtNewDay(timer);
        } else {
            throw new Exception("Bad timer: " + timer.toString());
        }
    }

    private boolean isDayMode(Timer timer) {
        return true;
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

    private void startNow(Timer timer) {
        Log.e(TAG, "StartTimer");
        SoundSwitchUtil.soundTurnOff();
    }

    private void startAtTimeTo(Timer timer) {

        Log.e(TAG, "Current time = " + CalendarUtil.getCurrentTimeCalendar().getTimeInMillis());
        Log.e(TAG, "timer turn off in = " + CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        ).getTimeInMillis());

//        TODO: Improve this multiply code
        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, false),
                CalendarUtil.getCalendar(
                        TimeParser.getIntHourFromString(timer.getTimeTo()),
                        TimeParser.getIntMinuteFromString(timer.getTimeTo())
                ).getTimeInMillis()
        );
    }

    private void startAtNewDay(Timer timer) {


        Calendar newDay = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        newDay = CalendarUtil.addDay(newDay);

        Log.e(TAG, "Current time = " + CalendarUtil.getCurrentTimeCalendar().getTimeInMillis());
        Log.e(TAG, "Sound turn on in new day = " + newDay.getTimeInMillis());

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                newDay.getTimeInMillis()
        );
    }

    private void startAtTimeFrom(Timer timer) {

        Log.e(TAG, "Current time = " + CalendarUtil.getCurrentTimeCalendar().getTimeInMillis());
        Log.e(TAG, "timer start from = " + CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        ).getTimeInMillis());

        AlarmManagerUtil.startAlarm(
                IntentTimerUtil.getIntentFromTimer(timer, true),
                CalendarUtil.getCalendar(
                        TimeParser.getIntHourFromString(timer.getTimeFrom()),
                        TimeParser.getIntMinuteFromString(timer.getTimeFrom())
                ).getTimeInMillis()
        );
    }

    private boolean isCurrentTimeLessThenTimerFrom(@NonNull Timer timer) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );
        return CalendarUtil.isCurrentLessThenTimeFrom(from);
    }

    private boolean isTimeBetweenFromAndTo(@NonNull Timer timer) {
        Calendar from = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeFrom()),
                TimeParser.getIntMinuteFromString(timer.getTimeFrom())
        );
        Calendar to = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        return CalendarUtil.isCurrentTimeBetweenFromAndTo(from, to);
    }

    private boolean isCurrentMoreThenTimeTo(@NonNull Timer timer) {
        Calendar to = CalendarUtil.getCalendar(
                TimeParser.getIntHourFromString(timer.getTimeTo()),
                TimeParser.getIntMinuteFromString(timer.getTimeTo())
        );
        return CalendarUtil.isCurrentMoreThenTimeTo(to);
    }


}
