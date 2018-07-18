package ru.agrass.silenttimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    private final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();
    private TimerScheduler timerScheduler;
    private DataBaseImpl db;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        timerScheduler = new TimerScheduler();
        if (intent == null) {
            Log.e(TAG, "Intent == null");
            return;
        }
        boolean isSoundOff = intent.getBooleanExtra(IntentTimerUtil.TAG_SOUND_TURN_OFF, false);
        if (isSoundOff) {
            soundTurnOff(intent);
            return;
        }
        soundTurnOn(intent);
        updateTimer(intent.getLongExtra(TAG_UID,0L), context);
    }

    private void soundTurnOn(Intent intent) {
        SoundSwitchUtil.soundTurnOn();
        Log.e(TAG, String.valueOf(intent.getLongExtra(TAG_UID,0L)));
    }

    private void soundTurnOff(Intent intent) {
        SoundSwitchUtil.soundTurnOff();
        Log.e(TAG, String.valueOf(intent.getLongExtra(TAG_UID,0L)));
    }

    private void updateTimer(long uid, Context context) {
        db = DataBaseImpl.getInstance(context);
        db.getTimer(uid)
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(timer -> timerScheduler.startTimer(timer))
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }
}
