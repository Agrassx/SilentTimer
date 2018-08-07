package ru.agrass.silenttimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Observable;

import io.reactivex.disposables.Disposable;
import ru.agrass.silenttimer.model.DataBaseImpl;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.model.TimerScheduler;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;

public class OnBootReceiver extends BroadcastReceiver {

    private static final String TAG = OnBootReceiver.class.getSimpleName();

    private TimerScheduler timerScheduler;
    private final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();
    private DataBaseImpl db;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        if (intent.getAction() == null) return;

        timerScheduler = new TimerScheduler();
        db = DataBaseImpl.getInstance(context);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            restartTimers();
        }
    }

    private void restartTimers() {
        Log.e(TAG, "Boot complete !");
        db.getAllTimers()
                .subscribeOn(mSchedulerProvider.io())
                .flatMapIterable(list -> list)
                .filter(Timer::isEnable)
                .doOnNext(timerScheduler::setUpTimer)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
    }
}
