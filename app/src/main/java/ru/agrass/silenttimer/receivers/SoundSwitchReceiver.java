package ru.agrass.silenttimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.reactivex.Observable;
import ru.agrass.silenttimer.model.database.DataBaseImpl;
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
        Log.i(TAG, "onReceive");

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
            Log.i(TAG, SWITCH_TIMER);
            switchTimer(intent);
            return;
        }

        if (intent.getAction().equals(STOP_TIMER)) {
            Log.i(TAG, STOP_TIMER);
            stopTimer(intent);
        }
    }

    private void stopTimer(Intent intent) {
        long uid = intent.getLongExtra(TAG_UID,0L);
        Observable.fromCallable(() -> db.getTimer(uid))
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(timer -> {
                    timer.setEnable(false);
                    db.insertOrUpdateTimer(timer);
                })
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }

    private void switchTimer(Intent intent) {
        boolean isSoundOff = intent.getBooleanExtra(IntentTimerUtil.TAG_SOUND_TURN_OFF, false);
        if (isSoundOff) {
            SoundSwitchUtil.soundTurnOff();
        } else {
            SoundSwitchUtil.soundTurnOn();
        }
        startAtNextTime(intent.getLongExtra(TAG_UID, 0L));
    }

    private void startAtNextTime(long uid) {
        Observable.fromCallable(() -> db.getTimer(uid))
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(timer -> timerScheduler.updateTimer(timer))
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }
}
