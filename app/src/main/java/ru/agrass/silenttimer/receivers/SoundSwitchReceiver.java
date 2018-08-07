package ru.agrass.silenttimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import ru.agrass.silenttimer.model.DataBaseImpl;
import ru.agrass.silenttimer.model.TimerScheduler;
import ru.agrass.silenttimer.utils.IntentTimerUtil;
import ru.agrass.silenttimer.utils.SoundSwitchUtil;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;
import static ru.agrass.silenttimer.utils.IntentTimerUtil.TAG_UID;

public class SoundSwitchReceiver extends BroadcastReceiver {
    private static final String TAG = SoundSwitchReceiver.class.getSimpleName();

    public static final String SWITCH_TIMER = "ru.agrass.silenttimer.action.switch";
    public static final String STOP_TIMER = "ru.agrass.silenttimer.action.stop";
    private final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();
    private TimerScheduler timerScheduler;
    private DataBaseImpl db;

    @Override
    public void onReceive(Context context, Intent intent) {
//        TODO: change log.e on log.i
        Log.e(TAG, "onReceive");

        db = DataBaseImpl.getInstance(context);
        timerScheduler = new TimerScheduler();

        if (intent == null) {
            Log.e(TAG, "Intent == null");
            Crashlytics.log(TAG + ": Intent == null");
            return;
        }

        if (intent.getAction() == null) {
            Log.e(TAG, "Intent action == null");
            Crashlytics.log(TAG + ": Intent action == null");
            return;
        }

        if (intent.getAction().equals(SWITCH_TIMER)) {
            switchTimer(intent);
            return;
        }

        if (intent.getAction().equals(STOP_TIMER)) {
            stopTimer(intent);
        }
    }

    private void stopTimer(Intent intent) {
        db.getTimer(intent.getLongExtra(TAG_UID,0L))
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(timer -> {
                    timer.setEnable(false);
                    db.insertOrUpdateTimer(timer)
                        .subscribe();
                })
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }

    private void switchTimer(Intent intent) {
        boolean isSoundOff = intent.getBooleanExtra(IntentTimerUtil.TAG_SOUND_TURN_OFF, false);
        if (isSoundOff) {
            soundTurnOff(intent);
            return;
        }
        soundTurnOn(intent);
        startAtNextTime(intent.getLongExtra(TAG_UID,0L));
    }

    private void soundTurnOn(Intent intent) {
        SoundSwitchUtil.soundTurnOn();
        Log.e(TAG, String.valueOf(intent.getLongExtra(TAG_UID,0L)));
    }

    private void soundTurnOff(Intent intent) {
        SoundSwitchUtil.soundTurnOff();
        Log.e(TAG, String.valueOf(intent.getLongExtra(TAG_UID,0L)));
    }

    private void startAtNextTime(long uid) {
        db.getTimer(uid)
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(timer -> timerScheduler.setUpTimer(timer))
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }
}
