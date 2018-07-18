package ru.agrass.silenttimer.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import ru.agrass.silenttimer.SilentTimerApplication;

import static ru.agrass.silenttimer.utils.IntentTimerUtil.TAG_UID;

public class AlarmManagerUtil {

    private static final String TAG = AlarmManagerUtil.class.getSimpleName();

    public static void startAlarm(Intent intent, long atTime) {
        Log.e(TAG, "Start alarm");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                SilentTimerApplication.getInstance().getApplicationContext(),
                safeLongToInt(intent.getLongExtra(TAG_UID, 0L)),
                intent,
                0
        );
        SilentTimerApplication
                .getAlarmManager()
                .setExact(
                        AlarmManager.RTC_WAKEUP,
                        atTime,
                        alarmIntent
                );
    }

    public static void cancelAlarm(Intent intent) {
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                SilentTimerApplication.getInstance(),
                safeLongToInt(intent.getLongExtra(TAG_UID, 0L)),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        SilentTimerApplication.getAlarmManager().cancel(alarmIntent);
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}
